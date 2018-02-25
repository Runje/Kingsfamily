package com.koenig.commonModel


import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 06.10.2017.
 */

abstract class Item : Byteable {
    var id: String
    var name: String

    override val byteLength: Int
        get() = Byteable.Companion.getStringLength(id) + Byteable.Companion.getStringLength(name)

    constructor(name: String) {
        this.name = name
        id = UUID.randomUUID().toString()
    }

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    constructor(buffer: ByteBuffer) {
        id = Byteable.byteToString(buffer)
        name = Byteable.byteToString(buffer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val item = other as Item?

        return id == item!!.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun writeBytes(buffer: ByteBuffer) {
        buffer.put(Byteable.Companion.stringToByte(id))
        Byteable.Companion.writeString(name, buffer)
    }

}
