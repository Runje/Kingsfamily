package blue.koenig.kingsfamilylibrary.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import blue.koenig.kingsfamilylibrary.model.shared.FamilyContentProvider
import com.koenig.FamilyConstants.NO_DATE_LONG
import com.koenig.commonModel.Byteable
import com.koenig.commonModel.User
import net.iharder.Base64
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 18.09.2017.
 */

object FamilyConfig {
    private val SHARED_PREF = "KINGSFAMILY_SHARED_PREF"
    private val SPECIFIC_PREF = "KINGSFAMILY_SPECIFIC_PREF"
    private val USERID = "USERID"
    val NO_ID = ""
    val DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY")
    val NEVER = "-"
    private val LAST_SYNC_DATE = "LASTSYNCDATE"
    private val START_DATE = "STARTDATE"


    private val FAMILY_MEMBERS = "FAMILY_MEMBERS"
    private val FAMILY_NAME = "FAMILY_NAME"
    private val USER_NAME = "USER_NAME"
    private val logger = LoggerFactory.getLogger("FamilyConfig")
    private var userId = NO_ID

    val userIdObservable = BehaviorSubject.create<String>()

    fun init(context: Context) {
        userIdObservable.onNext(getUserId(context))
    }

    /**
     * Gets the userId of the User.
     * @return Name of the User
     */
    fun getUserId(context: Context? = null): String {
        if (userId == NO_ID) {
            check(context != null)
            val id = FamilyContentProvider.get(context, USERID)
            userId = id
        }

        return userId
    }

    /**
     * Saves the userId to the shared preferences
     * @param userId
     * @param context
     */
    fun saveUserId(userId: String, context: Context) {
        logger.info("Setting user id to " + userId)
        FamilyContentProvider.put(context, USERID, userId)
        FamilyConfig.userId = userId
        userIdObservable.onNext(userId)
    }

    /**
     * Gets the last sync date from shared prefs.
     *
     * @param context
     * @return
     */
    fun getLastSyncDate(context: Context, name: String): DateTime {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return DateTime(preferences.getLong(LAST_SYNC_DATE + name, NO_DATE_LONG))
    }

    /**
     * Saves the last sync date to shared prefs.
     *
     * @param date
     * @param context
     */
    fun saveLastSyncDate(date: DateTime, context: Context, name: String) {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putLong(LAST_SYNC_DATE + name, date.millis).commit()
    }

    fun getFamilyMembers(context: Context): List<User> {
        val membersString = FamilyContentProvider.get(context, FAMILY_MEMBERS)
        if (membersString.isEmpty()) {
            return ArrayList()
        }

        try {
            val buffer = ByteBuffer.wrap(Base64.decode(membersString))
            val size = buffer!!.short
            val members = ArrayList<User>(size.toInt())
            for (i in 0 until size) {
                members.add(User(buffer))
            }
            return members
        } catch (e: IOException) {
            logger.error("Error decoding family members")
            return ArrayList()
        }

    }

    fun setFamilyMembers(context: Context, members: List<User>) {
        val buffer = ByteBuffer.allocate(Byteable.getListLength(members))
        Byteable.writeList(members, buffer)
        val membersString = Base64.encodeBytes(buffer.array())
        FamilyContentProvider.put(context, FAMILY_MEMBERS, membersString)
    }

    fun saveBytes(context: Context, bytes: ByteArray, key: String) {
        val byteString = Base64.encodeBytes(bytes)
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putString(key, byteString).commit()
    }

    fun getBytesFromConfig(context: Context, key: String): ByteBuffer? {
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

    fun getStartDate(context: Context): DateTime {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return DateTime(preferences.getLong(START_DATE, DateTime(2015, 1, 1, 0, 0).millis))
    }

    fun saveStartDate(date: DateTime, context: Context) {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putLong(START_DATE, date.millis).commit()
    }
}
