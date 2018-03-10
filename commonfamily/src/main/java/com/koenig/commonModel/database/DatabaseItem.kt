package com.koenig.commonModel.database

import com.koenig.commonModel.Byteable
import com.koenig.commonModel.Item

import org.joda.time.DateTime

import java.nio.ByteBuffer

/**
 * Created by Thomas on 24.11.2015.
 */
class DatabaseItem<T : Item> : Byteable {
    var item: T
        protected set
    var isDeleted: Boolean = false
    var insertDate: DateTime
    var lastModifiedDate: DateTime
    var lastModifiedId: String
    var insertId: String

    var id: String
        get() = item.id
        set(value) {
            item.id = value
        }

    var name: String
        get() = item.name
        set(value) {
            item.name = value
        }

    override val byteLength: Int
        get() = Byteable.Companion.dateLength * 2 + Byteable.Companion.getStringLength(lastModifiedId) + Byteable.Companion.getStringLength(insertId) + boolLength + Byteable.Companion.getItemLength(item)

    constructor(item: T, insertDate: DateTime, lastModified: DateTime, deleted: Boolean, insertId: String, lastModifiedId: String) {
        this.item = item
        this.insertDate = insertDate
        this.lastModifiedDate = lastModified
        this.isDeleted = deleted
        this.insertId = insertId
        this.lastModifiedId = lastModifiedId
    }

    constructor(item: T, insertId: String, lastModifiedId: String) : this(item, DateTime.now(), DateTime.now(), insertId, lastModifiedId)

    constructor(item: T, insertDate: DateTime, lastModifiedDate: DateTime, insertId: String, lastModifiedId: String) {
        this.item = item
        this.insertDate = insertDate
        this.lastModifiedDate = lastModifiedDate
        this.isDeleted = false
        this.insertId = insertId
        this.lastModifiedId = lastModifiedId
    }

    constructor(item: T, id: String) : this(item, id, id)

    constructor(buffer: ByteBuffer) {
        lastModifiedDate = Byteable.Companion.byteToDateTime(buffer)
        insertDate = Byteable.Companion.byteToDateTime(buffer)
        insertId = Byteable.Companion.byteToString(buffer)
        lastModifiedId = Byteable.Companion.byteToString(buffer)
        isDeleted = Byteable.Companion.byteToBoolean(buffer)
        // Eventuell static create erstellen
        item = Byteable.byteToItem<T>(buffer)
    }

    override fun toString(): String {
        return "LGADatabaseItem{" +
                "deleted=" + isDeleted +
                ", id=" + item.id +
                ", insertDate=" + insertDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedId='" + lastModifiedId + '\''.toString() +
                ", insertId='" + insertId + '\''.toString() +
                '}'.toString()
    }

    override fun writeBytes(buffer: ByteBuffer) {
        Byteable.Companion.writeDateTime(lastModifiedDate, buffer)
        Byteable.Companion.writeDateTime(insertDate, buffer)
        Byteable.Companion.writeString(insertId, buffer)
        Byteable.Companion.writeString(lastModifiedId, buffer)
        Byteable.Companion.writeBool(isDeleted, buffer)
        Byteable.Companion.writeItem(item, buffer)
    }
}
