package com.koenig.commonModel.finance

import com.koenig.commonModel.*

import org.joda.time.LocalDate

import java.nio.ByteBuffer

/**
 * Created by Thomas on 21.10.2017.
 */

class Expenses : BookkeepingEntry {

    var day: LocalDate
    // id of executing standingOrder
    var standingOrder: String

    var isCompensation: Boolean

    override val byteLength: Int
        get() = super.byteLength + LocalDate().byteLength + Byteable.Companion.getStringLength(standingOrder) + Byteable.boolLength

    constructor(name: String, category: String, subCategory: String, costs: Int, costDistribution: CostDistribution, date: LocalDate, standingOrderId: String, isCompensation: Boolean = false) : super(name, category, subCategory, costs, costDistribution) {
        this.day = date
        this.standingOrder = standingOrderId
        this.isCompensation = isCompensation
    }

    constructor(entry: BookkeepingEntry, date: LocalDate, standingOrderId: String, isCompensation: Boolean = false) : super(entry) {
        this.day = date
        this.standingOrder = standingOrderId
        this.isCompensation = isCompensation
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        day = buffer.localDate
        standingOrder = Byteable.Companion.byteToString(buffer)
        isCompensation = buffer.boolean
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        day.writeBytes(buffer)
        buffer.put(Byteable.Companion.stringToByte(standingOrder))
        buffer.putBoolean(isCompensation)
    }

    override fun toString(): String {
        return "Expenses(day=$day, standingOrder='$standingOrder', isCompensation=$isCompensation, bookkeepingEntry=${super.toString()})"
    }


    override val isValid: Boolean
        get() {
            return super.isValid && Validator.dayIsReasonable(day) && Validator.isEmptyOrId(standingOrder)
        }


}
