package com.koenig.communication


import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Component
import com.koenig.commonModel.Item
import com.koenig.communication.messages.*
import com.koenig.communication.messages.family.CreateUserMessage
import com.koenig.communication.messages.family.FamilyMemberMessage
import com.koenig.communication.messages.family.UserMessage
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * Created by Thomas on 11.01.2017.
 */

object Parser {
    internal var logger = LoggerFactory.getLogger("Parser")
    fun parse(buffer: ByteBuffer): FamilyMessage? {
        val version = buffer.int
        val component = Component.read(buffer)
        val name = Byteable.byteToString(buffer)
        val fromId = Byteable.byteToString(buffer)
        val toId = Byteable.byteToString(buffer)
        val messageId = Byteable.byteToString(buffer)

        var msg: FamilyMessage? = null
        when (name) {
            TextMessage.NAME -> msg = TextMessage(version, component, fromId, toId, messageId, buffer)

            FamilyMemberMessage.NAME -> msg = FamilyMemberMessage(version, component, fromId, toId, messageId, buffer)

            CreateUserMessage.NAME -> msg = CreateUserMessage(version, component, fromId, toId, messageId, buffer)

            UserMessage.NAME -> msg = UserMessage(version, component, fromId, toId, messageId, buffer)
            UpdatesMessage.NAME -> msg = UpdatesMessage<Item>(version, component, fromId, toId, messageId, buffer)
            AskForUpdatesMessage.NAME -> msg = AskForUpdatesMessage(version, component, fromId, toId, messageId, buffer)
            AUDMessage.NAME -> msg = AUDMessage(version, component, fromId, toId, messageId, buffer)

            else -> logger.error("Unknown name: $name")
        }

        return msg
    }

}
