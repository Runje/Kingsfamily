package com.koenig.commonModel.Repository

import com.koenig.commonModel.Goal
import com.koenig.commonModel.Item
import com.koenig.commonModel.database.DatabaseItem
import com.koenig.commonModel.finance.BankAccount
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder
import io.reactivex.Observable
import org.joda.time.DateTime
import java.sql.SQLException

/**
 * Created by Thomas on 07.02.2018.
 */
interface ExpensesRepository : Repository<Expenses> {
    val compensations: Map<DateTime, Expenses>
}

interface StandingOrderRepository : Repository<StandingOrder> {
    @Throws(SQLException::class)
    fun addExpensesToStandingOrders(standingOrderId: String, expensesId: String, dateTime: DateTime)
}

interface GoalRepository : Repository<Goal>
interface BankAccountRepository : Repository<BankAccount>


interface Repository<T : Item> {
    val hasChanged: Observable<Boolean>
    /**
     * Observable for all changes of expenses
     */
    val allItemsObservable: Observable<List<T>>
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