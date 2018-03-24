package com.koenig.commonModel.database

import java.sql.SQLException
import java.util.concurrent.locks.Lock

interface DatabaseTable<T> {
    var lock: Lock
    val tableName: String
    val columnNames: List<String>

    @Throws(SQLException::class)
    fun isExisting(): Boolean

    val all: List<T>

    /**
     * Shall return the create statement for the specific tables field, i.e for field name and birthday:
     * name TEXT, birthday LONG
     *
     * @return
     */
    val tableSpecificCreateStatement: String

    fun buildCreateStatement(): String {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +

                tableSpecificCreateStatement +
                ");"
    }
    @Throws(SQLException::class)
    fun create()

    @Throws(SQLException::class)
    fun add(item: T)

    fun addAll(items: Collection<T>)

    @Throws(SQLException::class)
    fun deleteAllEntrys()

    @Throws(SQLException::class)
    fun runInLock(transaction: () -> Unit) {
        runInLock(Database.Transaction(transaction))
    }

    @Throws(SQLException::class)
    fun runInLock(runnable: Database.Transaction) {
        lock.lock()
        try {
            runnable.run()
        } finally {
            lock.unlock()
        }
    }

    @Throws(SQLException::class)
    fun <X> runInLockWithResult(runnable: () -> X): X {
        return runInLockWithResult(Database.ResultTransaction<X>(runnable))
    }

    @Throws(SQLException::class)
    fun <X> runInLockWithResult(runnable: Database.ResultTransaction<X>): X {
        lock.lock()
        try {
            return runnable.run()
        } finally {
            lock.unlock()
        }
    }
}