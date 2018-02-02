package com.koenig.communication.messages

import com.koenig.commonModel.*
import com.koenig.commonModel.Operator.*
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder
import java.nio.ByteBuffer

/**
 * Created by Thomas on 02.11.2017.
 */

class AUDMessage : FamilyMessage {
    var operation: Operation<out Item>
        internal set

    constructor(component: Component, operation: Operation<out Item>) : super(component) {
        this.operation = operation
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, operation: Operation<Item>) : super(version, component, fromId, toId) {
        this.operation = operation
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, buffer: ByteBuffer) : super(component) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        operation = Operation(buffer)
    }

    override fun getName(): String {
        return NAME
    }

    override fun getContentLength(): Int {
        return operation.byteLength
    }

    override fun writeContent(buffer: ByteBuffer) {
        operation.writeBytes(buffer)
    }

    companion object {
        const val NAME = "AUDMessage"

        private fun create(operation: Operator, item: Item): AUDMessage {
            val component: Component
            if (item is Expenses || item is Operation<*> || item is StandingOrder || item is Category || item is Goal) {
                component = Component.FINANCE
                return AUDMessage(component, Operation(operation, item))
            } else {
                throw RuntimeException("Unsupported item: " + item.toString())
            }
        }

        fun createAdd(item: Item): AUDMessage {
            return create(ADD, item)
        }

        fun createUpdate(item: Item): AUDMessage {
            return create(UPDATE, item)
        }

        fun createDelete(item: Item): AUDMessage {
            return create(DELETE, item)
        }
    }
}
