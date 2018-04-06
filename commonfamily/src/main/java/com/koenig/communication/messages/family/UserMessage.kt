package com.koenig.communication.messages.family

import com.koenig.commonModel.Component
import com.koenig.commonModel.User
import com.koenig.communication.messages.FamilyMessage

import java.nio.ByteBuffer

/**
 * Created by Thomas on 11.10.2017.
 */

class UserMessage : FamilyMessage {

    var user: User
        private set

    override val name: String
        get() = NAME


    override val contentLength: Int
        get() = user.byteLength


    constructor(user: User) : super(Component.FAMILY) {
        this.user = user
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(component, messageId) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        user = User(buffer)
    }

    override fun writeContent(buffer: ByteBuffer) {
        user.writeBytes(buffer)
    }

    override fun toString(): String {
        return "UserMessage{" +
                "user=" + user +
                '}'.toString()
    }

    companion object {

        val NAME = "UserMessage"
    }
}
