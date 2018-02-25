package com.koenig.commonModel.finance

import com.koenig.StringFormats
import com.koenig.commonModel.Byteable

import java.nio.ByteBuffer

/**
 * Created by Thomas on 22.10.2017.
 */

class Costs : Byteable {
    var Real: Int = 0
    var Theory: Int = 0

    override val byteLength: Int
        get() = 8

    constructor(real: Int, theory: Int) {
        Real = real
        Theory = theory
    }

    constructor(buffer: ByteBuffer) {
        Real = buffer.int
        Theory = buffer.int
    }

    override fun toString(): String {
        return if (Real != Theory) {
            StringFormats.centsToCentString(Real) + "/" + StringFormats.centsToCentString(Theory)
        } else StringFormats.centsToCentString(Real)

    }

    fun toEuroString(): String {
        return if (Real != Theory) {
            StringFormats.centsToEuroString(Real) + "/" + StringFormats.centsToEuroString(Theory)
        } else StringFormats.centsToEuroString(Real)
    }

    override fun writeBytes(buffer: ByteBuffer) {
        buffer.putInt(Real)
        buffer.putInt(Theory)
    }

    operator fun plus(costs: Costs): Costs {
        return Costs(costs.Real + this.Real, costs.Theory + this.Theory)
    }
}
