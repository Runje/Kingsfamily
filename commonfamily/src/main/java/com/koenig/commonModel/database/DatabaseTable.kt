package com.koenig.commonModel.database

import com.koenig.commonModel.Item
import com.koenig.commonModel.User
import com.koenig.communication.messages.FamilyMessage
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by Thomas on 25.11.2017.
 */

abstract class DatabaseTable<T : Item> {
    protected val logger = LoggerFactory.getLogger(javaClass.simpleName)
    var lock = ReentrantLock()

    abstract val tableName: String

    @Throws(SQLException::class)
    abstract fun getAll(): List<DatabaseItem<T>>

    @Throws(SQLException::class)
    abstract fun isExisting(): Boolean

    val allItems: List<T>
        get() = toItemList(getAll())

    val allItemsObservable = BehaviorSubject.create<List<T>>()
    val hasChanged = BehaviorSubject.create<Boolean>()
    protected abstract val columnNames: List<String>

    /**
     * Shall return the create statement for the specific tables field, i.e for field name and birthday:
     * name TEXT, birthday LONG
     *
     * @return
     */
    protected abstract val tableSpecificCreateStatement: String

    protected val baseColumnNames: List<String>
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
    abstract fun create()

    @Throws(SQLException::class)
    abstract fun add(databaseItem: DatabaseItem<T>)

    @Throws(SQLException::class)
    abstract fun getDatabaseItemFromId(id: String): DatabaseItem<T>?

    @Throws(SQLException::class)
    abstract fun getFromId(id: String): T?

    @Throws(SQLException::class)
    abstract fun deleteAllEntrys()

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

    @Throws(SQLException::class)
    protected fun runInLock(transaction: () -> Unit) {
        runInLock(Database.Transaction(transaction))
    }

    @Throws(SQLException::class)
    protected fun runInLock(runnable: Database.Transaction) {
        lock.lock()
        try {
            runnable.run()
        } finally {
            lock.unlock()
        }
    }

    @Throws(SQLException::class)
    protected fun <X> runInLockWithResult(runnable: () -> X): X {
        return runInLockWithResult(Database.ResultTransaction<X>(runnable))
    }

    @Throws(SQLException::class)
    protected fun <X> runInLockWithResult(runnable: Database.ResultTransaction<X>): X {
        lock.lock()
        try {
            return runnable.run()
        } finally {
            lock.unlock()
        }
    }

    protected fun buildCreateStatement(): String {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_DELETED + " INT, " +
                COLUMN_INSERT_DATE + " LONG, " +
                COLUMN_INSERT_ID + " TEXT, " +
                COLUMN_MODIFIED_DATE + " LONG, " +
                COLUMN_MODIFIED_ID + " TEXT, " +
                COLUMN_NAME + " TEXT" +
                tableSpecificCreateStatement +
                ");"
    }

    protected fun getUsers(userService: UserService, usersText: String): MutableList<User> {
        val users = ArrayList<User>()
        if (!usersText.isEmpty()) {
            val userIds = usersText.split(FamilyMessage.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (id in userIds) {
                try {
                    users.add(userService.getUserFromId(id))
                } catch (e: SQLException) {
                    // don't add user to result
                    logger.error("Couldn't find user with id: " + id)
                }

            }
        }

        return users
    }

    protected fun usersToId(users: List<User>): String {
        val builder = StringBuilder()
        for (user in users) {
            builder.append(user.id)
            builder.append(FamilyMessage.SEPARATOR)
        }

        return if (users.size > 0) builder.substring(0, builder.length - 1) else ""
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

}
