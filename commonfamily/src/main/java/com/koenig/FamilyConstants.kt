package com.koenig

import com.koenig.commonModel.User
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by Thomas on 19.11.2017.
 */

object FamilyConstants {
    val DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY")
    val NEVER = "-"
    val NO_ID = ""
    val NO_DATE_LONG: Long = 0
    val NO_DATE = DateTime(NO_DATE_LONG)
    val UNLIMITED = DateTime(3000, 12, 31, 23, 59)
    val GOAL_ALL_USER = User("ALL_GOAL", "ALL GOAL", "A", DateTime())
    var ALL_USER = User("ALL", "ALL", "A", DateTime())
}
