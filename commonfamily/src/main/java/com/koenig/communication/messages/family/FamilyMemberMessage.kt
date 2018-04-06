package com.koenig.communication.messages.family


import com.koenig.commonModel.Component
import com.koenig.commonModel.User
import com.koenig.communication.messages.FamilyMessage
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 11.01.2017.
 */

class FamilyMemberMessage : FamilyMessage {

    var members: MutableList<User>

    override val name: String
        get() = NAME


    override val contentLength: Int
        get() {
            var size = 0
            for (user in members) {
                size += user.byteLength
            }
            return 2 + size
        }

    constructor(members: MutableList<User>) : super(Component.FAMILY) {
        this.members = members
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(component, messageId) {
        this.version = version
        this.fromId = fromId
        this.toId = toId

        val size = buffer.short.toInt()
        members = ArrayList(size)
        for (i in 0 until size) {
            members.add(User(buffer))
        }
    }


    override fun writeContent(buffer: ByteBuffer) {
        buffer.putShort(members.size.toShort())
        for (user in members) {
            user.writeBytes(buffer)
        }
    }

    override fun toString(): String {
        return "FamilyMemberMessage{}"
    }

    companion object {

        val NAME = "FamilyMemberMessage"
    }
}
