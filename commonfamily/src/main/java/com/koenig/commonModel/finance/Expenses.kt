package com.koenig.commonModel.finance

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Validator
import com.koenig.commonModel.boolean
import com.koenig.commonModel.putBoolean

import org.joda.time.DateTime

import java.nio.ByteBuffer

/**
 * Created by Thomas on 21.10.2017.
 */

class Expenses : BookkeepingEntry {

    var date: DateTime
    // id of executing standingOrder
    var standingOrder: String

    var isCompensation: Boolean

    override val byteLength: Int
        get() = super.byteLength + Byteable.Companion.dateLength + Byteable.Companion.getStringLength(standingOrder) + Byteable.boolLength

    constructor(name: String, category: String, subCategory: String, costs: Int, costDistribution: CostDistribution, date: DateTime, standingOrderId: String, isCompensation: Boolean = false) : super(name, category, subCategory, costs, costDistribution) {
        this.date = date
        this.standingOrder = standingOrderId
        this.isCompensation = isCompensation
    }

    constructor(entry: BookkeepingEntry, date: DateTime, standingOrderId: String, isCompensation: Boolean = false) : super(entry) {
        this.date = date
        this.standingOrder = standingOrderId
        this.isCompensation = isCompensation
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        date = Byteable.Companion.byteToDateTime(buffer)
        standingOrder = Byteable.Companion.byteToString(buffer)
        isCompensation = buffer.boolean
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.put(Byteable.Companion.dateTimeToBytes(date))
        buffer.put(Byteable.Companion.stringToByte(standingOrder))
        buffer.putBoolean(isCompensation)
    }


    override val isValid: Boolean
        get() {
            return super.isValid && Validator.dateIsReasonable(date) && Validator.isEmptyOrId(standingOrder)
        }

    override fun toString(): String {
        return "Expenses(date=$date, standingOrder='$standingOrder', isCompensation=$isCompensation)"
    }
}
