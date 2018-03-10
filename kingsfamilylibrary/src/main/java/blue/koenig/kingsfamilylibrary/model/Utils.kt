package blue.koenig.kingsfamilylibrary.model

import android.content.Context
import android.content.pm.PackageManager
import com.koenig.FamilyConstants
import org.joda.time.DateTime
import org.joda.time.LocalDate

/**
 * Created by Thomas on 13.10.2017.
 */

object Utils {

    fun localDateToString(day: LocalDate): String {
        return if (day == FamilyConstants.NO_DAY) {
            FamilyConstants.NEVER
        } else day.toString(FamilyConstants.DATE_FORMAT)
    }

    fun stringToDateTime(date: String): DateTime {
        return if (date == FamilyConstants.NEVER) {
            FamilyConstants.NO_DATE
        } else DateTime.parse(date, FamilyConstants.DATE_FORMAT)
    }

    fun isAppInstalled(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }


}
