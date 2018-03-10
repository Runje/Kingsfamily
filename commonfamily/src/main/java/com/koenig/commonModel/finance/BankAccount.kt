package com.koenig.commonModel.finance

import com.koenig.FamilyConstants
import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Item
import com.koenig.commonModel.User
import org.joda.time.LocalDate
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 21.10.2015.
 */
class BankAccount : Item {
    var owners: MutableList<User>
    var bank: String
    /**
     * Sorted list where the first one is the newest
     */
    var balances: MutableList<Balance>

    // latest update of balances
    val dateTime: LocalDate
        get() = if (balances.size == 0) {
            FamilyConstants.BEGIN_LOCAL_DATE
        } else balances[0].day

    override val byteLength: Int
        get() = super.byteLength + Byteable.Companion.getListLength(owners) + Byteable.Companion.getStringLength(bank) + Byteable.Companion.getListLength(balances)

    val balance: Int
        get() = if (balances.size == 0) 0 else balances[0].balance

    constructor(id: String, name: String, bank: String, owners: MutableList<User>, balances: MutableList<Balance>) : super(id, name) {
        this.owners = owners
        this.name = name
        this.bank = bank
        this.balances = balances
        sortBalances(balances)
    }

    constructor(id: String, name: String, bank: String, owner: User, balances: MutableList<Balance>) : super(id, name) {
        this.owners = ArrayList(1)
        owners.add(owner)
        this.name = name
        this.bank = bank
        this.balances = balances
        sortBalances(balances)
    }

    constructor(name: String, bank: String, owners: MutableList<User>, balances: MutableList<Balance>) : super(name) {
        this.owners = owners
        this.name = name
        this.bank = bank
        this.balances = balances
        sortBalances(balances)
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        var size = buffer.short
        this.owners = ArrayList(size.toInt())
        for (i in 0 until size) {
            owners.add(User(buffer))
        }

        bank = Byteable.Companion.byteToString(buffer)
        size = buffer.short
        balances = ArrayList(size.toInt())
        for (i in 0 until size) {
            balances.add(Balance(buffer))
        }
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        Byteable.Companion.writeList(owners, buffer)
        Byteable.Companion.writeString(bank, buffer)
        Byteable.Companion.writeList(balances, buffer)
    }

    fun addBalance(balance: Balance) {
        balances.add(balance)
        sortBalances(balances)
    }

    override fun toString(): String {
        return "BankAccount{" +
                "id='" + id + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", owners=" + owners +
                ", bank='" + bank + '\''.toString() +
                ", balances=" + balances +
                '}'.toString()
    }


    fun toReadableString(): String {
        return bank + " - " + name
    }

    fun deleteBalance(balance: Balance) {
        var removeBalance: Balance? = null
        for (bal in balances) {
            if (bal.day == balance.day) {
                removeBalance = bal
                break
            }
        }

        balances.remove(removeBalance)
    }

    companion object {

        fun sortBalances(balances: List<Balance>) {
            // newest at top
            Collections.sort(balances) { lhs, rhs -> rhs.day.compareTo(lhs.day) }
        }
    }
}
