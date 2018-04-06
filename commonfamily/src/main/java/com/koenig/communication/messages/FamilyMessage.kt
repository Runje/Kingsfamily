package com.koenig.communication.messages


import com.example.Message
import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Component
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Thomas on 11.01.2017.
 */

abstract class FamilyMessage : Message {
    protected var logger = LoggerFactory.getLogger(javaClass.simpleName)
    lateinit var fromId: String
    lateinit var toId: String
    val messageId: String
    var component: Component
        protected set
    var version: Int = 0
        protected set


    abstract val name: String

    val totalLength: Int
        get() = (4 + 4 + component.bytesLength + Byteable.getStringLength(name) + Byteable.getStringLength(fromId) + Byteable.getStringLength(toId) + Byteable.getStringLength(messageId)
                + contentLength)

    protected abstract val contentLength: Int

    constructor(component: Component, messageId: String = UUID.randomUUID().toString()) {
        this.component = component
        this.version = VERSION_NUMBER
        this.messageId = messageId
    }

    constructor(version: Int, component: Component, fromId: String, toId: String, messageId: String = UUID.randomUUID().toString()) {
        this.component = component
        this.version = version
        this.fromId = fromId
        this.toId = toId
        this.messageId = messageId
    }

    protected abstract fun writeContent(buffer: ByteBuffer)

    protected fun headerToBuffer(buffer: ByteBuffer) {
        buffer.putInt(totalLength)
        buffer.putInt(version)
        buffer.put(component.toBytes())
        buffer.put(Byteable.stringToByte(name))
        buffer.put(Byteable.stringToByte(fromId))
        buffer.put(Byteable.stringToByte(toId))
        buffer.put(Byteable.stringToByte(messageId))
    }

    override fun getBuffer(): ByteBuffer {
        val buffer = ByteBuffer.allocate(totalLength)
        headerToBuffer(buffer)
        writeContent(buffer)
        buffer.flip()
        return buffer
    }

    companion object {
        val ServerId = "KOENIGSPUTZ_SERVER_ID"
        val SEPARATOR = ";"
        private val VERSION_NUMBER = 1
    }
}
