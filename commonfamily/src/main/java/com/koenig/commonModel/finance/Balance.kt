package com.koenig.commonModel.finance

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Item
import org.joda.time.DateTime
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 23.12.2017.
 */

class Balance : Item {
    var balance: Int = 0
    var date: DateTime

    override val byteLength: Int
        get() = super.byteLength + 4 + Byteable.dateLength

    constructor(id: String, balance: Int, dateTime: DateTime) : super(id, Integer.toString(balance)) {
        this.balance = balance
        this.date = dateTime
    }

    constructor(balance: Int, dateTime: DateTime) : super(Integer.toString(balance)) {
        this.balance = balance
        this.date = dateTime
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        balance = buffer.int
        date = Byteable.Companion.byteToDateTime(buffer)
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.putInt(balance)
        Byteable.Companion.writeDateTime(date, buffer)
    }

    override fun toString(): String {
        return "Balance{" +
                "balance=" + balance +
                ", date=" + date +
                '}'.toString()
    }

    companion object {

        fun getBalances(bytes: ByteArray): MutableList<Balance> {
            val buffer = ByteBuffer.wrap(bytes)
            val size = buffer.int
            val balances = ArrayList<Balance>(size)
            for (i in 0 until size) {
                balances.add(Balance(buffer))
            }

            return balances
        }

        fun listToBytes(balances: List<Balance>): ByteArray {
            var size = 4
            for (balance in balances) {
                size += balance.byteLength
            }

            val buffer = ByteBuffer.allocate(size)
            buffer.putInt(balances.size)
            for (balance in balances) {
                balance.writeBytes(buffer)
            }
            return buffer.array()
        }
    }
}
