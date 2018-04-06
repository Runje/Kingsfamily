package com.koenig.communication.messages.family

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Component
import com.koenig.communication.messages.FamilyMessage

import org.joda.time.DateTime

import java.nio.ByteBuffer

/**
 * Created by Thomas on 11.10.2017.
 */

class CreateUserMessage : FamilyMessage {

    var userName: String
        private set
    var birthday: DateTime
        private set

    override val name: String
        get() = NAME


    override val contentLength: Int
        get() = Byteable.getStringLength(userName) + Byteable.dateLength

    constructor(userName: String, birthday: DateTime) : super(Component.FAMILY) {
        this.birthday = birthday
        this.userName = userName
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(version, component, fromId, toId, messageId) {
        userName = Byteable.byteToString(buffer)
        birthday = Byteable.byteToDateTime(buffer)
    }

    override fun writeContent(buffer: ByteBuffer) {
        buffer.put(Byteable.stringToByte(userName))
        buffer.put(Byteable.dateTimeToBytes(birthday))
    }

    override fun toString(): String {
        return "CreateUserMessage{" +
                "userName='" + userName + '\''.toString() +
                ", birthday=" + birthday +
                '}'.toString()
    }

    companion object {

        val NAME = "CreateUserMessage"
    }
}
