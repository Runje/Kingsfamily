package com.koenig.commonModel

import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 17.11.2017.
 */

class Category : Item {
    private var subs: MutableList<String>? = null

    override val byteLength: Int
        get() {
            var subsLength = 2
            for (sub in subs!!) {
                subsLength += Byteable.Companion.getStringLength(sub)
            }

            return super.byteLength + subsLength
        }

    constructor(main: String, subs: MutableList<String>) : super(main) {
        this.subs = subs
    }

    constructor(main: String) : super(main) {
        this.subs = ArrayList()
    }

    constructor(id: String, main: String, subs: MutableList<String>) : super(id, main) {
        this.subs = subs
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        val size = buffer.short
        subs = ArrayList(size.toInt())
        for (i in 0 until size) {
            subs!!.add(Byteable.Companion.byteToString(buffer))
        }
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.putShort(subs!!.size.toShort())
        for (sub in subs!!) {
            Byteable.Companion.writeString(sub, buffer)
        }
    }

    fun getSubs(): List<String> {
        return ArrayList(subs!!)
    }

    fun setSubs(subs: MutableList<String>) {
        this.subs = subs
    }

    fun hasSubs(): Boolean {
        return subs!!.size > 0
    }

    fun addSub(sub: String) {
        subs!!.add(sub)
    }
}
