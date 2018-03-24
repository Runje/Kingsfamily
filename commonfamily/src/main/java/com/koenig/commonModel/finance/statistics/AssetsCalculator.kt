package com.koenig.commonModel.finance.statistics

import com.google.common.collect.Lists
import com.koenig.FamilyConstants
import com.koenig.FamilyConstants.ALL_USER
import com.koenig.commonModel.Repository.AssetsRepository
import com.koenig.commonModel.User
import com.koenig.commonModel.finance.Balance
import com.koenig.commonModel.finance.BankAccount
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.*
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by Thomas on 03.01.2018.
 */

class AssetsCalculator(bankSubject: ItemSubject<BankAccount>, startDateObservable: Observable<YearMonth>, endDateObservable: Observable<YearMonth>, val assetsRepository: AssetsRepository) {
    lateinit var startDate: YearMonth
    lateinit var endDate: YearMonth
    val yearsList: List<String>
    private var logger = LoggerFactory.getLogger(javaClass.simpleName)
    private var absoluteAssetsMap: MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>> = assetsRepository.load()
    private var lock = ReentrantLock()
    val allAssets: BehaviorSubject<MutableMap<YearMonth, MonthStatistic>>
    val deltaAssetsForAll: BehaviorSubject<Map<YearMonth, MonthStatistic>> = BehaviorSubject.create()

    val entrysForAll: MutableMap<YearMonth, MonthStatistic>
        get() {
            return absoluteAssetsMap[ALL_ASSETS] ?: return mutableMapOf()
        }

    val entrysForForecast: MutableMap<YearMonth, MonthStatistic>
        get() {
            return absoluteAssetsMap[YEAR_FORECAST] ?: return mutableMapOf()
        }

    val entrysForFutureForecast: MutableMap<YearMonth, MonthStatistic>
        get() {
            return absoluteAssetsMap[FUTURE_FORECAST] ?: return mutableMapOf()
        }

    init {
        startDateObservable.subscribe { startDate = it }
        endDateObservable.subscribe { endDate = it }
        allAssets = BehaviorSubject.createDefault(absoluteAssetsMap[ALL_ASSETS] ?: mutableMapOf())
        calcDeltaMapFromAll(entrysForAll)
        bankSubject.addAddListener({ bankAccount -> addBankAccount(bankAccount) })
        bankSubject.addDeleteListener({ bankAccount -> deleteBankAccount(bankAccount!!) })
        bankSubject.addUpdateListener({ _, newBankAccount -> updateBankAccount(newBankAccount) })
        yearsList = generateYearsList()
    }

    fun calcStatisticsFor(startMonth: YearMonth, endMonth: YearMonth, withYearForecast: Boolean = false): AssetsStatistics {
        val filteredEntrys = entrysForAll.filter { it.key in startMonth..endMonth }.toMutableMap()

        var overallWin = 0
        var monthlyWin = 0
        if (filteredEntrys.isNotEmpty()) {
            val last = filteredEntrys.values.maxBy { it.month }!!
            val first = filteredEntrys.values.minBy { it.month }!!
            overallWin = last[ALL_USER] - first[ALL_USER]
            monthlyWin = overallWin / Months.monthsBetween(first.month, last.month).months
        }

        // fill last values with values from forecast(first from forecast is same as last from actual entries)
        if (withYearForecast) {
            entrysForForecast.values.forEach {
                filteredEntrys[it.month] = (filteredEntrys[it.month]
                        ?: MonthStatistic(it.month)) + it
            }
        }

        return AssetsStatistics(startMonth, endMonth, filteredEntrys, monthlyWin, overallWin)
    }

    private fun generateYearsList(): List<String> {
        val list = Lists.newArrayList(FamilyConstants.OVERALL_STRING, FamilyConstants.FUTURE_STRING)
        list.addAll(createYearsList(startDate, YearMonth()))
        return list
    }


    private fun addBankAccount(bankAccount: BankAccount) {
        updateStatisticsFor(bankAccount)

    }

    private fun updateStatisticsFor(bankAccount: BankAccount, remove: Boolean = false) {
        lock.lock()
        if (remove) {
            absoluteAssetsMap.remove(bankAccount)
        } else {
            absoluteAssetsMap[bankAccount] = calculateStatisticsOfBankAccount(bankAccount, startDate, endDate)
        }
        updateAllAssetsEntry()
        updateForecast()
        calcDeltaMapFromAll(entrysForAll)
        assetsRepository.save(absoluteAssetsMap)
        lock.unlock()
    }

    private fun updateForecast() {
        if (entrysForAll.isEmpty()) return
        val lastEntry = entrysForAll.values.maxBy { it.month }!!

        // update until end of actual year
        val lastMonthOfYearForecast = lastEntry.month.plusYears(1).withMonthOfYear(1)
        val yearForecast = mutableMapOf<YearMonth, MonthStatistic>()
        val sum = lastEntry[ALL_USER]
        val entryMap = HashMap<User, Int>(1)

        entryMap[FORECAST_USER] = sum
        // first entry of yearForecast is last entry of actual entries
        yearForecast[lastEntry.month] = MonthStatistic(lastEntry.month, entryMap)

        // get average win of last 12 month
        var overallWin = lastEntry.copy()
        // difference of last 12 month (use earliest statistics if 12 months back is not available
        overallWin -= (entrysForAll[lastEntry.month.minusMonths(12)]
                ?: entrysForAll.values.minBy { it.month }!!)
        val averageWinPerMonth = overallWin[ALL_USER] / 12.0

        var nextPrognose = sum.toDouble()
        yearMonthRange(lastEntry.month.plusMonths(1), lastMonthOfYearForecast).forEach {
            nextPrognose += averageWinPerMonth
            val map = HashMap<User, Int>(1)
            map[FORECAST_USER] = nextPrognose.toInt()
            yearForecast[it] = MonthStatistic(it, map)
        }

        absoluteAssetsMap[YEAR_FORECAST] = yearForecast

        val years = 20
        val futureForecast = mutableMapOf<YearMonth, MonthStatistic>()

        // first entry is last entry of year yearForecast
        futureForecast[lastMonthOfYearForecast] = yearForecast[lastMonthOfYearForecast]!!
        yearMonthRange(lastMonthOfYearForecast.plusYears(1), lastMonthOfYearForecast.plusYears(years), stepSizeInMonth = 12).forEach {
            nextPrognose += averageWinPerMonth * 12
            val map = HashMap<User, Int>(1)
            map[FORECAST_USER] = nextPrognose.toInt()
            futureForecast[it] = MonthStatistic(it, map)
        }

        absoluteAssetsMap[FUTURE_FORECAST] = futureForecast

    }


    private fun deleteBankAccount(bankAccount: BankAccount) {
        updateStatisticsFor(bankAccount, remove = true)
    }

    /**
     * Updates the ALL_ASSETS entry(it does not update all assets!)
     */
    private fun updateAllAssetsEntry() {
        val allEntries = mutableMapOf<YearMonth, MonthStatistic>()
        // Don't need to initialize because
        for ((bankAccount, map) in absoluteAssetsMap) {
            // sum up only normal bank accounts
            if (bankAccount == ALL_ASSETS || bankAccount == YEAR_FORECAST || bankAccount == FUTURE_FORECAST)
                continue

            map.values.forEach {
                // add to all assets
                val sumStatistic = (allEntries[it.month]
                        ?: MonthStatistic(it.month)) + (map[it.month] ?: MonthStatistic(it.month))

                // calculate sum for ALL_USER
                val entryMap = sumStatistic.entryMap.toMutableMap()
                entryMap[ALL_USER] = entryMap.filter { it.key != ALL_USER }.values.sum()
                allEntries[it.month] = MonthStatistic(it.month, entryMap)
            }
        }

        absoluteAssetsMap[ALL_ASSETS] = allEntries
        logger.info("On Next")
        allAssets.onNext(allEntries)
    }

    private fun calcDeltaMapFromAll(allEntries: Map<YearMonth, MonthStatistic>): MutableMap<YearMonth, MonthStatistic> {
        // convert to year month delta map
        val deltaMap = mutableMapOf<YearMonth, MonthStatistic>()
        yearMonthRange(startDate, endDate).forEach {
            val thisMonth = allEntries[it] ?: (allEntries[it.plusMonths(1)] ?: MonthStatistic(it))
            val nextMonth = allEntries[it.plusMonths(1)] ?: (allEntries[it] ?: MonthStatistic(it))
            deltaMap[it] = (nextMonth - thisMonth).withMonth(it)
        }

        deltaAssetsForAll.onNext(deltaMap)

        return deltaMap
    }

    private fun updateBankAccount(newBankAccount: BankAccount) {
        updateStatisticsFor(newBankAccount)
    }

    fun getEntrysFor(bankAccount: BankAccount): Map<YearMonth, MonthStatistic>? {
        return absoluteAssetsMap[bankAccount]
    }

    companion object {
        val dayOfMonthDeadline = 1
        val ALL_ASSETS = BankAccount("ALL_ASSETS", "ALL_ASSETS", "ALL", ALL_USER, ArrayList())
        var FORECAST_USER = User("YEAR_FORECAST", "YEAR_FORECAST", "F", DateTime())
        var YEAR_FORECAST = BankAccount("YEAR_FORECAST", "YEAR_FORECAST", "YEAR_FORECAST", FORECAST_USER, ArrayList())
        var FUTURE_FORECAST = BankAccount("FUTURE_FORECAST", "FUTURE_FORECAST", "FUTURE_FORECAST", FORECAST_USER, ArrayList())

        /**
         * Calculates the absolute credit of the account for each yearMonth at the 1. of each month. It calculate the linear estimation between two balances. At the beginning and end it is assumed to be constant.
         * Example: 1.3 50 €, 1.5 100 € --> Statistics for April are from 1.4 = 75 €
         */
        fun calculateStatisticsOfBankAccount(bankAccount: BankAccount, start: YearMonth, end: YearMonth): MutableMap<YearMonth, MonthStatistic> {
            val map = mutableMapOf<YearMonth, MonthStatistic>()


            // balances are sorted with newest at top
            val balances = bankAccount.balances
            if (balances.size == 0) {
                yearMonthRange(start, end).forEach {
                    map[it] = MonthStatistic(it)
                }
                return map
            }


            // oldest balance is last (sorted)
            val firstBalance = balances.last()
            var firstMonth = firstBalance.day.yearMonth

            // if it is after dueday the month after is the first month
            if (firstBalance.day.dayOfMonth().get() > dayOfMonthDeadline) {
                firstMonth = firstMonth.plusMonths(1)
            }

            // It is constant before the first entry (always the same balance)
            yearMonthRange(start, firstMonth.minusMonths(1)).forEach {
                map[it] = MonthStatistic(it, firstBalance.toEntryMap(bankAccount.owners))
            }


            var nextMonth = firstMonth
            var balanceBefore = firstBalance

            for (i in balances.indices.reversed()) {
                val balance = balances[i]
                val date = balance.day
                val isSameDay = date.yearMonth == nextMonth && date.dayOfMonth().get() == dayOfMonthDeadline
                if (isSameDay) {
                    map[nextMonth] = MonthStatistic(nextMonth, balance.toEntryMap(bankAccount.owners))
                    nextMonth = nextMonth.plusMonths(1)
                } else if (date.isAfterDeadlineOf(nextMonth)) {
                    // calculate value for each month before until nextMonth is after the day of the actual balance
                    while (date.isAfterDeadlineOf(nextMonth)) {
                        map[nextMonth] = MonthStatistic(nextMonth, distributeValueToEntryMap(calcLinearEstimation(balanceBefore, balance, nextMonth.toLocalDate(dayOfMonthDeadline)), bankAccount.owners))
                        nextMonth = nextMonth.plusMonths(1)
                    }
                }

                balanceBefore = balance
            }

            // set value of all following balances until end to last value
            yearMonthRange(nextMonth, end).forEach {
                map[it] = MonthStatistic(it, balanceBefore.toEntryMap(bankAccount.owners))
            }

            return map
        }

        fun calcLinearEstimation(lastBalance: Balance, balance: Balance, nextDate: LocalDate): Int {
            val days = Days.daysBetween(lastBalance.day, balance.day).days
            val daysUntil = Days.daysBetween(lastBalance.day, nextDate).days

            return lastBalance.balance + daysUntil * (balance.balance - lastBalance.balance) / days
        }
    }

    fun getStartValueFor(user: User): Int {
        return getEntrysFor(ALL_ASSETS)?.minBy { it.key }?.value?.get(user) ?: 0
    }


}

private fun LocalDate.isAfterDeadlineOf(nextMonth: YearMonth): Boolean {
    return yearMonth > nextMonth || ((yearMonth == nextMonth && dayOfMonth().get() > AssetsCalculator.dayOfMonthDeadline))
}

private fun Balance.toEntryMap(owners: MutableList<User>): Map<User, Int> {
    return distributeValueToEntryMap(balance, owners)
}

private fun distributeValueToEntryMap(value: Int, owners: List<User>): Map<User, Int> {
    val entryMap = mutableMapOf<User, Int>()
    // distribute equally
    val n = owners.size
    var distributed = 0
    var valuePart = value / n
    for (i in 0 until n) {
        val user = owners[i]
        if (i == n - 1) {
            // last one gets the rest
            valuePart = value - distributed
        }

        entryMap[user] = valuePart
        distributed += valuePart
    }

    return entryMap
}

val LocalDate.yearMonth: YearMonth
    get() {
        return YearMonth(year, monthOfYear)
    }

fun createYearsList(startDate: YearMonth, endDate: YearMonth): List<String> {
    val list = ArrayList<String>()
    var nextDate = startDate
    while (nextDate.isBefore(endDate)) {
        list.add(0, Integer.toString(nextDate.year))
        nextDate = nextDate.plus(Years.ONE)
    }

    return list
}