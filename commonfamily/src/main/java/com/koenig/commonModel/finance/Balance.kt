package com.koenig.commonModel.finance

import com.koenig.commonModel.Item
import com.koenig.commonModel.byteLength
import com.koenig.commonModel.localDate
import com.koenig.commonModel.writeBytes
import org.joda.time.LocalDate
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 23.12.2017.
 */

class Balance : Item {
    var balance: Int = 0
    var day: LocalDate

    override val byteLength: Int
        get() = super.byteLength + 4 + day.byteLength

    constructor(id: String, balance: Int, day: LocalDate) : super(id, Integer.toString(balance)) {
        this.balance = balance
        this.day = day
    }

    constructor(balance: Int, day: LocalDate) : super(Integer.toString(balance)) {
        this.balance = balance
        this.day = day
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        balance = buffer.int
        day = buffer.localDate
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.putInt(balance)
        day.writeBytes(buffer)
    }

    override fun toString(): String {
        return "Balance{" +
                "balance=" + balance +
                ", day=" + day +
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
            val size = 4 + balances.sumBy { it.byteLength }

            val buffer = ByteBuffer.allocate(size)
            buffer.putInt(balances.size)
            for (balance in balances) {
                balance.writeBytes(buffer)
            }
            return buffer.array()
        }
    }
}
