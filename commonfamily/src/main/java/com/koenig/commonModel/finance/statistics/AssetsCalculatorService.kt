package com.koenig.commonModel.finance.statistics

import com.koenig.commonModel.finance.BankAccount
import org.joda.time.LocalDate
import org.joda.time.YearMonth

/**
 * Created by Thomas on 05.01.2018.
 */

interface AssetsCalculatorService {

    val startDate: LocalDate

    val endDate: LocalDate

    fun loadAbsoluteBankAccountStatistics(): MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>>

    fun save(absoluteMap: Map<BankAccount, MutableMap<YearMonth, MonthStatistic>>)
}
