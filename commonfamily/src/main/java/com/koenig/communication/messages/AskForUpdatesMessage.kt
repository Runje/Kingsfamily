package com.koenig.communication.messages

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Component
import com.koenig.commonModel.ItemType

import org.joda.time.DateTime

import java.nio.ByteBuffer

/**
 * Created by Thomas on 30.11.2017.
 */

class AskForUpdatesMessage : FamilyMessage {
    var lastSyncDate: DateTime
        internal set
    var updateType: ItemType
        internal set

    override val name: String
        get() = NAME


    override val contentLength: Int
        get() = Byteable.dateLength + Byteable.getEnumLength(updateType)

    constructor(component: Component, lastSyncDate: DateTime, updateType: ItemType) : super(component) {
        this.lastSyncDate = lastSyncDate
        this.updateType = updateType
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(component, messageId) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        this.lastSyncDate = DateTime(buffer.long)
        this.updateType = Byteable.byteToEnum(buffer, ItemType::class.java)
    }

    override fun writeContent(buffer: ByteBuffer) {
        Byteable.writeDateTime(lastSyncDate, buffer)
        Byteable.writeEnum(updateType, buffer)
    }

    override fun toString(): String {
        return "AskForUpdatesMessage{" +
                "lastSyncDate=" + lastSyncDate +
                ", updateType='" + updateType + '\''.toString() +
                '}'.toString()
    }

    companion object {
        val NAME = "AskForUpdatesMessage"

        fun askForExpenses(lastSyncDate: DateTime): AskForUpdatesMessage {
            return AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.EXPENSES)
        }

        fun askForStandingOrders(lastSyncDate: DateTime): AskForUpdatesMessage {
            return AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.STANDING_ORDER)
        }

        fun askForCategorys(lastSyncDate: DateTime): AskForUpdatesMessage {
            return AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.CATEGORY)
        }
    }
}
