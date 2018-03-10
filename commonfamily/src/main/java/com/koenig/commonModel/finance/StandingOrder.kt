package com.koenig.commonModel.finance

import com.koenig.commonModel.*
import org.joda.time.LocalDate
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 22.10.2017.
 */

class StandingOrder : BookkeepingEntry {
    var executedExpenses: MutableMap<LocalDate, String>
    var firstDate: LocalDate
    var endDate: LocalDate
    var frequency: Frequency
    var frequencyFactor: Int = 0
    val lastExecutedExpenses
        get () = executedExpenses.maxBy { (day, _) -> day }?.value

    override val byteLength: Int
        get() {
            var mapSize = 2
            executedExpenses.forEach { (date, id) ->
                mapSize += date.byteLength
                mapSize += id.byteLength
            }
            return super.byteLength + Byteable.Companion.dateLength + Byteable.Companion.dateLength + Byteable.Companion.getEnumLength(frequency) + 4 + mapSize
        }

    constructor(name: String, category: String, subCategory: String, costs: Int, costDistribution: CostDistribution, firstDate: LocalDate, endDate: LocalDate, frequency: Frequency, frequencyFactor: Int, executedExpenses: MutableMap<LocalDate, String>) : super(name, category, subCategory, costs, costDistribution) {
        this.firstDate = firstDate
        this.frequency = frequency
        this.frequencyFactor = frequencyFactor
        this.executedExpenses = executedExpenses
        this.endDate = endDate
    }

    constructor(entry: BookkeepingEntry, firstDate: LocalDate, endDate: LocalDate, frequency: Frequency, frequencyFactor: Int, executedExpenses: MutableMap<LocalDate, String>) : super(entry) {
        this.firstDate = firstDate
        this.frequency = frequency
        this.frequencyFactor = frequencyFactor
        this.executedExpenses = executedExpenses
        this.endDate = endDate
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        firstDate = buffer.localDate
        endDate = buffer.localDate
        frequency = Byteable.Companion.byteToEnum(buffer, Frequency::class.java)
        frequencyFactor = buffer.int
        val size = buffer.short
        executedExpenses = mutableMapOf()
        for (i in 0 until size) {
            executedExpenses[buffer.localDate] = buffer.string
        }
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        firstDate.writeBytes(buffer)
        endDate.writeBytes(buffer)
        Byteable.Companion.writeEnum(frequency, buffer)
        buffer.putInt(frequencyFactor)
        buffer.putShort(executedExpenses.size.toShort())
        executedExpenses.forEach { (day, id) ->
            day.writeBytes(buffer)
            buffer.putString(id)
        }
    }

    override fun toString(): String {
        return "StandingOrder{" + super.toString() +
                "firstDate=" + firstDate +
                ", endDate=" + endDate +
                ", frequency=" + frequency +
                ", frequencyFactor=" + frequencyFactor +
                ", executedExpenses=" + executedExpenses +
                '}'.toString()
    }
}

fun calcUuidFrom(uuid: String): String {
    return UUID.nameUUIDFromBytes(uuid.toByteArray()).toString()
}

