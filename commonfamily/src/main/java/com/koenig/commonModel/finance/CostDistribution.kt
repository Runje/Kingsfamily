package com.koenig.commonModel.finance

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.User
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 22.10.2017.
 */

class CostDistribution : Byteable {
    internal var distribution: MutableMap<User, Costs>

    val isValid: Boolean
        get() = sumReal() == sumTheory()

    override val byteLength: Int
        get() {
            var size = 4
            for (user in distribution.keys) {
                size += user.byteLength + distribution[user]!!.byteLength
            }

            return size
        }

    constructor(distribution: MutableMap<User, Costs>) {
        this.distribution = distribution
    }

    constructor() {
        distribution = HashMap()
    }

    constructor(buffer: ByteBuffer) : this() {
        val size = buffer.int
        for (i in 0 until size) {
            val user = User(buffer)
            val costs = Costs(buffer)
            distribution[user] = costs
        }
    }

    fun getDistribution(): Map<User, Costs> {
        return distribution
    }

    fun sumReal(): Int {
        var sumReal = 0
        for (c in distribution.values) {
            sumReal += c.Real
        }

        return sumReal
    }

    fun sumTheory(): Int {
        var sumTheory = 0
        for (c in distribution.values) {
            sumTheory += c.Theory
        }

        return sumTheory
    }

    fun getCostsFor(user: User): Costs {
        val costs = distribution[user]
        return costs ?: Costs(0, 0)
    }


    fun putCosts(user: User, costs: Costs) {
        distribution[user] = costs
    }

    fun putCosts(user: User, real: Int, theory: Int = real) {
        putCosts(user, Costs(real, theory))
    }

    override fun toString(): String {
        if (distribution.isEmpty()) {
            return "Empty CostDistribution"
        }
        val builder = StringBuilder()
        for (user in distribution.keys) {
            val costs = distribution[user]
            builder.append(user.name)
            builder.append(": ")
            builder.append(costs)
            builder.append("\n")
        }

        return builder.substring(0, builder.length - 1)
    }

    fun getRealPercent(user: User): Float {
        val sum = sumReal()
        return if (sum == 0) {
            0f
        } else getCostsFor(user).Real / sum.toFloat()

    }

    fun getTheoryPercent(user: User): Float {
        val sum = sumTheory()
        return if (sum == 0) {
            0f
        } else getCostsFor(user).Theory / sum.toFloat()

    }

    override fun writeBytes(buffer: ByteBuffer) {
        buffer.putInt(distribution.size)
        for (user in distribution.keys) {
            user.writeBytes(buffer)
            buffer.put(distribution[user]!!.bytes)
        }
    }

    operator fun get(user: User): Costs {
        return getCostsFor(user)
    }

    operator fun set(user: User, costs: Costs) {
        distribution[user] = costs
    }
}
