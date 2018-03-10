package com.koenig.commonModel.database

import com.koenig.FamilyConstants
import com.koenig.commonModel.finance.statistics.MonthStatistic
import org.joda.time.YearMonth

interface MonthStatisticTable : DatabaseTable<MonthStatistic> {
    companion object {
        const val MONTH = "month"
        const val ENTRY_MAP = "entry_map"
    }

    override val columnNames: List<String>
        get() = listOf(MONTH, ENTRY_MAP)
    override val tableSpecificCreateStatement: String
        get() = " $MONTH INT PRIMARY KEY, $ENTRY_MAP BLOB"

    val allAsMap: Map<YearMonth, MonthStatistic>
        get() = all.associateBy({ it.month }, { it })

    fun overwriteAll(values: Collection<MonthStatistic>) {
        deleteAllEntrys()
        addAll(values)
    }

}

fun yearMonthFromInt(int: Int): YearMonth {
    return FamilyConstants.BEGIN_YEAR_MONTH.plusMonths(int)
}