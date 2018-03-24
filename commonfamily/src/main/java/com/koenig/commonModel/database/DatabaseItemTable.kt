package com.koenig.commonModel.database

import com.koenig.commonModel.Item
import com.koenig.commonModel.User
import com.koenig.commonModel.finance.statistics.ItemSubject
import com.koenig.communication.messages.FamilyMessage
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime
import java.sql.SQLException
import java.util.*

/**
 * Created by Thomas on 25.11.2017.
 */

interface DatabaseItemTable<T : Item> : ItemSubject<T>, DatabaseTable<DatabaseItem<T>> {

    var onDeleteListeners: MutableList<OnDeleteListener<T>>
    var onAddListeners: MutableList<OnAddListener<T>>
    var onUpdateListeners: MutableList<OnUpdateListener<T>>
    val hasChanged: BehaviorSubject<Boolean>

    val itemSpecificCreateStatement: String
    override val tableSpecificCreateStatement: String
        get() = DatabaseItemTable.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DatabaseItemTable.COLUMN_DELETED + " INT, " +
                DatabaseItemTable.COLUMN_INSERT_DATE + " LONG, " +
                DatabaseItemTable.COLUMN_INSERT_ID + " TEXT, " +
                DatabaseItemTable.COLUMN_MODIFIED_DATE + " LONG, " +
                DatabaseItemTable.COLUMN_MODIFIED_ID + " TEXT, " +
                DatabaseItemTable.COLUMN_NAME + " TEXT" + itemSpecificCreateStatement
    val allItems: List<T>
        get() = toItemList(all)


    val baseColumnNames: List<String>
        get() {
            val columns = ArrayList<String>()
            columns.add(COLUMN_ID)
            columns.add(COLUMN_DELETED)
            columns.add(COLUMN_INSERT_DATE)
            columns.add(COLUMN_INSERT_ID)
            columns.add(COLUMN_MODIFIED_DATE)
            columns.add(COLUMN_MODIFIED_ID)
            columns.add(COLUMN_NAME)
            return columns
        }

    @Throws(SQLException::class)
    fun getDatabaseItemFromId(id: String): DatabaseItem<T>?

    @Throws(SQLException::class)
    fun getFromId(id: String): T?

    @Throws(SQLException::class)
    fun addFrom(item: T, userId: String) {
        runInLock(Database.Transaction {
            val now = DateTime.now()
            val databaseItem = DatabaseItem(item, now, now, false, userId, userId)
            add(databaseItem)
            hasChanged.onNext(true)
        })
    }

    fun toItemList(list: List<DatabaseItem<out T>>): List<T> {
        val users = ArrayList<T>(list.size)
        list.mapTo(users) { it.item }
        return users
    }


    fun getUsers(userService: (String) -> User?, usersText: String): MutableList<User> {
        val users = ArrayList<User>()
        if (!usersText.isEmpty()) {
            val userIds = usersText.split(FamilyMessage.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (id in userIds) {
                try {
                    val user = userService.invoke(id)
                    user?.let { users.add(it) } ?: throw SQLException("User with id $id not found")
                } catch (e: SQLException) {
                    // don't add user to result
                    // TODO: logger.error("Couldn't find user with id: " + id)
                }

            }
        }

        return users
    }

    fun usersToId(users: List<User>): String {
        val builder = StringBuilder()
        for (user in users) {
            builder.append(user.id)
            builder.append(FamilyMessage.SEPARATOR)
        }

        return if (users.isNotEmpty()) builder.substring(0, builder.length - 1) else ""
    }

    companion object {
        val COLUMN_INSERT_DATE = "insert_date"
        val COLUMN_MODIFIED_DATE = "modified_date"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "name"
        val COLUMN_INSERT_ID = "insert_id"
        val COLUMN_MODIFIED_ID = "modified_id"
        val COLUMN_DELETED = "deleted"
        val FALSE = 0
        val TRUE = 1
        val FALSE_STRING = "0"
        val TRUE_STRING = "1"
        val STRING_LIST_SEPARATOR = ";"

        fun buildStringList(list: List<String>): String {
            if (list.size > 0) {
                val builder = StringBuilder()
                for (s in list) {
                    builder.append(s + STRING_LIST_SEPARATOR)
                }

                return builder.substring(0, builder.length - 1)
            } else {
                return ""
            }
        }

        fun getStringList(listAsString: String): MutableList<String> {
            if (listAsString.isEmpty()) {
                return ArrayList()
            }

            val items = listAsString.split(STRING_LIST_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // need to make a copy to allow List.add method! (Alternative create list  manually)
            return ArrayList(Arrays.asList(*items))
        }
    }

    override fun addDeleteListener(listener: OnDeleteListener<T>) {
        onDeleteListeners.add(listener)
    }

    override fun addAddListener(listener: OnAddListener<T>) {
        onAddListeners.add(listener)
    }

    override fun addUpdateListener(listener: OnUpdateListener<T>) {
        onUpdateListeners.add(listener)
    }

    fun removeDeleteListener(deleteListener: OnDeleteListener<T>) {
        onDeleteListeners.remove(deleteListener)
    }

    fun removeAddListener(addListener: OnAddListener<T>) {
        onAddListeners.remove(addListener)
    }

    fun removeUpdateListener(updateListener: OnUpdateListener<T>) {
        onUpdateListeners.remove(updateListener)
    }


    interface OnDeleteListener<T> {
        fun onDelete(item: T?)
    }

    interface OnUpdateListener<T> {
        fun onUpdate(oldItem: T?, newItem: T)
    }

    interface OnAddListener<T> {
        fun onAdd(item: T)
    }

}
