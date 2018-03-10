package com.koenig.commonModel.Repository

import com.koenig.commonModel.Goal
import com.koenig.commonModel.Item
import com.koenig.commonModel.database.DatabaseItem
import com.koenig.commonModel.database.MonthStatisticTable
import com.koenig.commonModel.finance.BankAccount
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder
import com.koenig.commonModel.finance.statistics.AssetsCalculator
import com.koenig.commonModel.finance.statistics.MonthStatistic
import io.reactivex.Observable
import org.joda.time.LocalDate
import org.joda.time.YearMonth
import java.sql.SQLException

/**
 * Created by Thomas on 07.02.2018.
 */
interface ExpensesRepository : Repository<Expenses> {
    val compensations: Map<LocalDate, Expenses>
}

interface StandingOrderRepository : Repository<StandingOrder> {
    @Throws(SQLException::class)
    fun addExpensesToStandingOrders(standingOrderId: String, expensesId: String, day: LocalDate)
}

interface GoalRepository : Repository<Goal> {
    fun getGoalFor(category: String, month: YearMonth): Double
    fun getGoalFor(category: String, year: Int): Int

}
interface BankAccountRepository : Repository<BankAccount>
interface AssetsRepository {
    fun load(): MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>>
    fun save(map: MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>>)
}

interface AssetsDbRepository : AssetsRepository {
    val bankAccountRepository: BankAccountRepository
    val tableCreator: (name: String) -> MonthStatisticTable
    override fun load(): MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>> {
        // get all BankAccounts
        val bankAccounts = bankAccountRepository.allItems.toMutableList()
        // + Future, YearForecast, All_Assets
        bankAccounts.add(AssetsCalculator.ALL_ASSETS)
        bankAccounts.add(AssetsCalculator.FUTURE_FORECAST)
        bankAccounts.add(AssetsCalculator.YEAR_FORECAST)

        val result = mutableMapOf<BankAccount, MutableMap<YearMonth, MonthStatistic>>()
        bankAccounts.forEach {
            // create table
            val table = tableCreator.invoke(it.toTableName())
            table.create()
            result[it] = table.allAsMap.toMutableMap()
        }

        return result
    }

    private fun BankAccount.toTableName(): String {
        return id + "_assets"
    }

    override fun save(map: MutableMap<BankAccount, MutableMap<YearMonth, MonthStatistic>>) {
        for ((bankAccount, statisticsMap) in map) {
            val table = tableCreator.invoke(bankAccount.toTableName())
            table.create()
            table.overwriteAll(statisticsMap.values)
        }
    }
}

interface IncomeRepository {
    fun loadDeltaMap(): MutableMap<YearMonth, MonthStatistic>
    fun loadAbsoluteMap(): MutableMap<YearMonth, MonthStatistic>
    fun saveDeltaMap(map: MutableMap<YearMonth, MonthStatistic>)
    fun saveAbsoluteMap(map: MutableMap<YearMonth, MonthStatistic>)
}

class IncomeDbRepository(val deltaRepository: MonthStatisticsRepository, val absoluteRepository: MonthStatisticsRepository) : IncomeRepository {
    override fun loadDeltaMap(): MutableMap<YearMonth, MonthStatistic> {
        return deltaRepository.load()
    }

    override fun loadAbsoluteMap(): MutableMap<YearMonth, MonthStatistic> {
        return absoluteRepository.load()
    }

    override fun saveDeltaMap(map: MutableMap<YearMonth, MonthStatistic>) {
        deltaRepository.save(map)
    }

    override fun saveAbsoluteMap(map: MutableMap<YearMonth, MonthStatistic>) {
        absoluteRepository.save(map)
    }

}

class MonthStatisticDbRepository(val table: MonthStatisticTable) : MonthStatisticsRepository {
    override fun load(): MutableMap<YearMonth, MonthStatistic> {
        table.create()
        return table.all.map { it.month to it }.toMap().toMutableMap()
    }

    override fun save(map: MutableMap<YearMonth, MonthStatistic>) {
        table.create()
        table.deleteAllEntrys()
        table.addAll(map.values)
    }

}

interface MonthStatisticsRepository {
    fun load(): MutableMap<YearMonth, MonthStatistic>
    fun save(map: MutableMap<YearMonth, MonthStatistic>)
}

interface CategoryRepository {
    val allCategoryAbsoluteStatistics: MutableMap<YearMonth, MonthStatistic>
    val allCategoryDeltaStatistics: MutableMap<YearMonth, MonthStatistic>

    fun getCategoryAbsoluteStatistics(category: String): MutableMap<YearMonth, MonthStatistic>
    fun getCategoryDeltaStatistics(category: String): MutableMap<YearMonth, MonthStatistic>

    fun saveAllCategoryAbsoluteStatistics(map: MutableMap<YearMonth, MonthStatistic>)
    fun saveAllCategoryDeltaStatistics(map: MutableMap<YearMonth, MonthStatistic>)

    fun saveCategoryAbsoluteStatistics(category: String, map: MutableMap<YearMonth, MonthStatistic>)
    fun saveCategoryDeltaStatistics(category: String, deltaMap: MutableMap<YearMonth, MonthStatistic>)
    val savedCategorys: Collection<String>
    val hasChanged: Observable<Any>
}

interface Repository<T : Item> {
    val hasChanged: Observable<Boolean>

    val allItems: List<T>


    @Throws(SQLException::class)
    fun updateFromServer(items: List<DatabaseItem<T>>)

    /**
     * Deletes the item from the user
     */
    fun delete(item: T)

    fun add(item: T)
    fun update(item: T)

    fun getFromId(id: String): T?
}