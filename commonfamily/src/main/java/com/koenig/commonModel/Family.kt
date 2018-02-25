package com.koenig.commonModel

import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 06.10.2017.
 */

class Family : Item {
    var users: MutableList<User>

    override val byteLength: Int
        get() = super.byteLength + Byteable.Companion.getListLength(users)

    constructor(name: String) : super(name) {
        users = ArrayList()
    }

    constructor(name: String, users: MutableList<User>) : super(name) {
        this.users = users
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        val size = buffer.short
        users = ArrayList(size.toInt())
        for (i in 0 until size) {
            users.add(User(buffer))
        }
    }

    constructor(id: String, name: String, users: MutableList<User>) : super(id, name) {
        this.users = users
    }


    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        Byteable.Companion.writeList(users, buffer)
    }

}
