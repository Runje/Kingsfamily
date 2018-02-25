package com.koenig.commonModel.finance

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Item
import com.koenig.commonModel.Validator

import java.nio.ByteBuffer

/**
 * Created by Thomas on 21.10.2017.
 */

open class BookkeepingEntry : Item {

    // userId --> costs
    var costDistribution: CostDistribution
    // costs in cent
    var costs: Int = 0
    var category: String
    var subCategory: String

    override val byteLength: Int
        get() = super.byteLength + Byteable.Companion.getStringLength(category) + Byteable.Companion.getStringLength(subCategory) + 4 + costDistribution.byteLength

    open// subcategory is allowed to be empty
    val isValid: Boolean
        get() = Validator.isNotEmpty(name) && Validator.isNotEmpty(category) &&
                costDistribution.isValid && costDistribution.sumReal() == costs


    constructor(name: String, category: String, subCategory: String, costs: Int, costDistribution: CostDistribution) : super(name) {
        this.costs = costs
        this.category = category
        this.subCategory = subCategory
        this.costDistribution = costDistribution
    }

    constructor(entry: BookkeepingEntry) : super(entry.name) {
        this.costs = entry.costs
        this.category = entry.category
        this.subCategory = entry.subCategory
        this.costDistribution = entry.costDistribution
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        category = Byteable.Companion.byteToString(buffer)
        subCategory = Byteable.Companion.byteToString(buffer)
        costs = buffer.int
        costDistribution = CostDistribution(buffer)
    }

    fun costsValid(): Boolean {
        return costDistribution.sumReal() == costs
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.put(Byteable.Companion.stringToByte(category))
        buffer.put(Byteable.Companion.stringToByte(subCategory))
        buffer.putInt(costs)
        costDistribution.writeBytes(buffer)
    }

    override fun toString(): String {
        return "BookkeepingEntry{" +
                "costs=" + costs +
                ", name='" + name + '\''.toString() +
                ", category='" + category + '\''.toString() +
                ", subCategory='" + subCategory + '\''.toString() +
                ", costDistribution=" + costDistribution +
                '}'.toString()
    }
}
