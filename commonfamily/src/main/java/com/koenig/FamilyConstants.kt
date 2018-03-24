package com.koenig

import com.koenig.commonModel.User
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.YearMonth
import org.joda.time.format.DateTimeFormat

/**
 * Created by Thomas on 19.11.2017.
 */

object FamilyConstants {
    val DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY")
    val NEVER = "-"
    val NO_ID = ""
    val NO_USER = User(NO_ID)
    val NO_DATE_LONG: Long = 0
    val NO_DATE = DateTime(NO_DATE_LONG)
    val NO_DAY = NO_DATE.toLocalDate()
    val UNLIMITED_DATETIME = DateTime(3000, 12, 31, 23, 59)
    val UNLIMITED = UNLIMITED_DATETIME.toLocalDate()
    val GOAL_ALL_USER = User("ALL_GOAL", "ALL GOAL", "A", DateTime())
    var ALL_USER = User("ALL", "ALL", "A", DateTime())
    val BEGIN_LOCAL_DATE: LocalDate = LocalDate(2015, 1, 1)
    const val OVERALL_STRING: String = "ALL"
    const val FUTURE_STRING: String = "FUTURE"
    val BEGIN_YEAR_MONTH: YearMonth = YearMonth(1970, 1)
    val COMPENSATION_NAME: String = "COMPENSATION"
    val COMPENSATION_CATEGORY: String = "COMPENSATION"
}
