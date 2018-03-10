package com.koenig.commonModel.finance.statistics

import com.koenig.FamilyConstants
import org.joda.time.LocalDate
import org.joda.time.YearMonth


/**
 * Created by Thomas on 08.01.2018.
 */

data class AssetsStatistics(val startDate: YearMonth = FamilyConstants.BEGIN_YEAR_MONTH, val endDate: YearMonth = LocalDate().yearMonth, val assets: Map<YearMonth, MonthStatistic> = mapOf(), val monthlyWin: Int = 0, val overallWin: Int = 0)
