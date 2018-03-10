package blue.koenig.kingsfamilylibrary.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import blue.koenig.kingsfamilylibrary.model.shared.FamilyContentProvider
import com.koenig.FamilyConstants.NO_DATE_LONG
import com.koenig.commonModel.*
import net.iharder.Base64
import org.joda.time.DateTime
import org.joda.time.YearMonth
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Created by Thomas on 18.09.2017.
 */

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
     * Gets the last sync day from shared prefs.
     *
     * @param context
     * @return
     */
    override fun getLastSyncDate(key: String): DateTime {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return DateTime(preferences.getLong(LAST_SYNC_DATE + key, NO_DATE_LONG))
    }

    /**
     * Saves the last sync day to shared prefs.
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

    override fun loadStartDate(): YearMonth {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        return preferences.getInt(START_DATE, YearMonth(2015, 1).toInt()).toYearMonth()
    }

    override fun saveStartDate(date: YearMonth) {
        val preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE)
        preferences.edit().putInt(START_DATE, date.toInt()).apply()
    }
}
