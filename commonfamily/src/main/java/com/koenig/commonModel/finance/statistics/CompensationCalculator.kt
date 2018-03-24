package com.koenig.commonModel.finance.statistics

import com.koenig.commonModel.Repository.ExpensesRepository
import com.koenig.commonModel.User
import com.koenig.commonModel.finance.CostDistribution
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.FinanceConfig
import io.reactivex.Observable
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.YearMonth

/**
 * Created by Thomas on 14.02.2018.
 */
class CompensationCalculator(private val expensesRepository: ExpensesRepository, expensesStatisticsObservable: Observable<Map<YearMonth, MonthStatistic>>, allAssetsObservable: Observable<Map<YearMonth, MonthStatistic>>, private val config: FinanceConfig) {
    private lateinit var startDate: YearMonth
    private lateinit var deltaAssets: Map<YearMonth, MonthStatistic>
    private lateinit var expensesDeltaStatistics: Map<YearMonth, MonthStatistic>
    private lateinit var users: List<User>

    init {
        config.startDateObservable.subscribe { startDate = it }
        allAssetsObservable.subscribe { deltaAssets = it }
        expensesStatisticsObservable.subscribe { expensesDeltaStatistics = it }
        config.familyMembersObservable.subscribe { users = it }

    }

    fun calcCompensations() {
        val compensations = calcCompensations(startDate, DateTime.now().yearMonth, deltaAssets, expensesDeltaStatistics, users, config.compensationName, config.compensationCategory)
        val oldCompensations = expensesRepository.compensations

        // update or add compensations
        compensations.forEach { new ->
            oldCompensations[new.day]?.let { old ->
                val distribution = CostDistribution()
                users.forEach { user ->
                    distribution[user] = old.costDistribution[user] + new.costDistribution[user]
                }
                old.costDistribution = distribution

                old.costs = old.costDistribution.sumTheory()

                // TODO: only update when changed
                // update
                expensesRepository.update(old)

            } ?: kotlin.run {
                // add
                expensesRepository.add(new)
            }
        }
    }

    companion object {
        fun calcCompensation(month: YearMonth, deltaAssets: MonthStatistic, deltaExpenses: MonthStatistic, users: List<User>, name: String, category: String): Expenses {
            val distribution = CostDistribution()
            for (user in users) {
                distribution.putCosts(user, deltaAssets[user] - deltaExpenses[user])
            }

            return Expenses(name, category, "", distribution.sumTheory(), distribution, month.toLastDayOfMonth(), "", true)
        }

        fun calcCompensations(start: YearMonth, end: YearMonth, deltaAssets: Map<YearMonth, MonthStatistic>, deltaExpenses: Map<YearMonth, MonthStatistic>, users: List<User>, name: String, category: String): List<Expenses> {
            val result = mutableListOf<Expenses>()
            val dates = yearMonthRange(start, end)

            if (deltaAssets.isEmpty()) return result
            if (deltaExpenses.isEmpty()) return result

            dates.forEach {
                result.add(calcCompensation(it, deltaAssets[it]
                        ?: MonthStatistic(it), deltaExpenses[it]
                        ?: MonthStatistic(it), users, name, category))
            }
            return result
        }
    }
}

fun YearMonth.toLastDayOfMonth(): LocalDate {
    return LocalDate(year, monthOfYear, 1).withDayAtEndOfMonth()
}

fun LocalDate.withDayAtEndOfMonth(): LocalDate {
    return withDayOfMonth(1).plusMonths(1).minusDays(1)
}

fun DateTime.withDayAtEndOfMonth(): DateTime {
    return withDayOfMonth(1).plusMonths(1).minusDays(1)
}
