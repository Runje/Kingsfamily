package blue.koenig.kingsfamilylibrary.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import blue.koenig.kingsfamilylibrary.model.shared.FamilyContentProvider
import com.koenig.FamilyConstants.NO_DATE_LONG
import com.koenig.FamilyConstants.NO_ID
import com.koenig.commonModel.Byteable
import com.koenig.commonModel.User
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.iharder.Base64
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Created by Thomas on 18.09.2017.
 */
interface FamilyConfig {
    var userId: String
    var familyMembers: List<User>
    var startDate: DateTime

    val userIdObservable: Observable<String>
    val familyMembersObservable: Observable<List<User>>
    val startDateObservable: Observable<DateTime>

    fun loadBuffer(key: String): ByteBuffer?
    fun saveBuffer(buffer: ByteBuffer, key: String)
    fun getLastSyncDate(key: String): DateTime
    fun saveLastSyncDate(date: DateTime, key: String)
}

abstract class FamilyConfigAbstract : FamilyConfig {
    protected val logger = LoggerFactory.getLogger("FamilyConfig")
    override var userId: String = NO_ID
        set(value) {
            field = value
            saveUserId(value)
            userIdObservable.onNext(value)
        }

    override var familyMembers: List<User> = emptyList()
        set(value) {
            field = value
            saveFamilyMembers(value)
            familyMembersObservable.onNext(value)
        }

    override var startDate: DateTime = DateTime()
        set(value) {
            field = value
            saveStartDate(value)
            startDateObservable.onNext(value)
        }

    override val userIdObservable = BehaviorSubject.create<String>()!!
    override val familyMembersObservable = BehaviorSubject.create<List<User>>()!!
    override val startDateObservable = BehaviorSubject.create<DateTime>()!!

    protected abstract fun loadUserId(): String
    protected abstract fun loadFamilyMembers(): List<User>
    protected abstract fun loadStartDate(): DateTime

    protected abstract fun saveUserId(userId: String)
    protected abstract fun saveStartDate(date: DateTime)
    protected abstract fun saveFamilyMembers(members: List<User>)

    fun init() {
        userId = loadUserId()
        familyMembers = loadFamilyMembers()
        startDate = loadStartDate()
    }
}

open class FamilyContextConfig(val context: Context) : FamilyConfigAbstract() {
    protected val SHARED_PREF = "KINGSFAMILY_SHARED_PREF"
    protected val SPECIFIC_PREF = "KINGSFAMILY_SPECIFIC_PREF"
    protected val USERID = "USERID"
    protected val LAST_SYNC_DATE = "LASTSYNCDATE"
    protected val START_DATE = "STARTDATE"
    protected val FAMILY_MEMBERS = "FAMILY_MEMBERS"
    protected val FAMILY_NAME = "FAMILY_NAME"
    protected val USER_NAME = "USER_NAME"

    override fun loadUserId(): String = FamilyContentProvider.get(context, USERID)
    override fun saveUserId(userId: String) = FamilyContentProvider.put(context, USERID, userId)

    /**
     * Gets the last sync date from shared prefs.
     *
     * @param context
     * @return
     */
    override fun getLastSyncDate(key: String): DateTime {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return DateTime(preferences.getLong(LAST_SYNC_DATE + key, NO_DATE_LONG))
    }

    /**
     * Saves the last sync date to shared prefs.
     *
     * @param date
     * @param context
     */
    override fun saveLastSyncDate(date: DateTime, key: String) {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putLong(LAST_SYNC_DATE + key, date.millis).apply()
    }

    override fun loadFamilyMembers(): List<User> {
        val membersString = FamilyContentProvider.get(context, FAMILY_MEMBERS)
        if (membersString.isEmpty()) {
            return ArrayList()
        }

        return try {
            val buffer = ByteBuffer.wrap(Base64.decode(membersString))
            val size = buffer!!.short
            val members = ArrayList<User>(size.toInt())
            for (i in 0 until size) {
                members.add(User(buffer))
            }
            members
        } catch (e: IOException) {
            logger.error("Error decoding family members")
            ArrayList()
        }
    }

    override fun saveFamilyMembers(members: List<User>) {
        val buffer = ByteBuffer.allocate(Byteable.getListLength(members))
        Byteable.writeList(members, buffer)
        val membersString = Base64.encodeBytes(buffer.array())
        FamilyContentProvider.put(context, FAMILY_MEMBERS, membersString)
    }

    override fun saveBuffer(buffer: ByteBuffer, key: String) {
        val byteString = Base64.encodeBytes(buffer.array())
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putString(key, byteString).apply()
    }

    override fun loadBuffer(key: String): ByteBuffer? {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        val byteString = preferences.getString(key, "")
        if (byteString == "") return null
        try {
            return ByteBuffer.wrap(Base64.decode(byteString!!))
        } catch (e: IOException) {
            logger.error("Error decoding bytes")
            return null
        }

    }

    override fun loadStartDate(): DateTime {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return DateTime(preferences.getLong(START_DATE, DateTime(2015, 1, 1, 0, 0).millis))
    }

    override fun saveStartDate(date: DateTime) {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putLong(START_DATE, date.millis).apply()
    }
}
