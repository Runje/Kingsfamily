package com.koenig.communication.messages


import com.koenig.commonModel.Component
import com.koenig.commonModel.Item
import com.koenig.commonModel.database.DatabaseItem
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 11.01.2017.
 */

class UpdatesMessage<T : Item> : FamilyMessage {
    var items: MutableList<DatabaseItem<T>>

    override val name: String
        get() = NAME


    override val contentLength: Int
        get() {
            var size = 2
            for (item in items) {
                size += item.byteLength
            }
            return size
        }

    constructor(items: MutableList<DatabaseItem<T>>) : super(Component.FINANCE) {
        this.items = items
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, items: MutableList<DatabaseItem<T>>) : super(component) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        this.items = items
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(component, messageId) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        val size = buffer.short.toInt()
        items = ArrayList(size)
        for (i in 0 until size) {
            items.add(DatabaseItem(buffer))
        }
    }


    override fun writeContent(buffer: ByteBuffer) {
        buffer.putShort(items.size.toShort())
        for (item in items) {
            item.writeBytes(buffer)
        }
    }

    override fun toString(): String {
        return "UpdatesMessage{" +
                "items=" + items +
                '}'.toString()
    }

    companion object {

        val NAME = "UpdatesMessage"
    }
}
