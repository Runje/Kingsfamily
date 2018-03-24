package com.koenig.commonModel

import com.koenig.FamilyConstants
import org.joda.time.YearMonth
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 06.10.2017.
 */

class Family : Item {
    var users: MutableList<User>
    var startMonth: YearMonth = FamilyConstants.BEGIN_YEAR_MONTH

    override val byteLength: Int
        get() = super.byteLength + Byteable.getListLength(users) + startMonth.byteLength

    constructor(name: String) : super(name) {
        users = ArrayList()
    }

    constructor(name: String, users: MutableList<User>, startDate: YearMonth) : super(name) {
        this.users = users
        this.startMonth = startDate
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        val size = buffer.short
        users = ArrayList(size.toInt())
        for (i in 0 until size) {
            users.add(User(buffer))
        }

        startMonth = buffer.yearMonth
    }

    constructor(id: String, name: String, users: MutableList<User>) : super(id, name) {
        this.users = users
    }


    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        Byteable.Companion.writeList(users, buffer)
        startMonth.writeBytes(buffer)
    }

}

