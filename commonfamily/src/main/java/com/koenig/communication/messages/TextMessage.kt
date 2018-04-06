package com.koenig.communication.messages


import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Component

import java.nio.ByteBuffer

/**
 * Created by Thomas on 11.01.2017.
 */

class TextMessage : FamilyMessage {


    var text: String
        internal set

    override val name: String
        get() = NAME

    override val contentLength: Int
        get() = Byteable.getStringLength(text)

    constructor(component: Component, text: String) : super(component) {
        this.text = text
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String, buffer: ByteBuffer) : super(component, messageId) {
        this.version = version
        this.fromId = fromId
        this.toId = toId
        this.text = Byteable.byteToString(buffer)
    }

    override fun writeContent(buffer: ByteBuffer) {
        buffer.put(Byteable.stringToByte(text))
    }

    override fun toString(): String {
        return "TextMessage{" +
                "text='" + text + '\''.toString() +
                '}'.toString()
    }

    companion object {

        val NAME = "TextMessage"
        val UPDATE_FAIL = "update_fail"
    }
}
