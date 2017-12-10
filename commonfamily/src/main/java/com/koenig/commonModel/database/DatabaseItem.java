package com.koenig.commonModel.database;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Item;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 24.11.2015.
 */
public class DatabaseItem<T extends Item> extends Byteable {
    protected T item;
    protected boolean deleted;
    protected DateTime insertDate;
    protected DateTime lastModifiedDate;
    protected String lastModifiedId;
    protected String insertId;

    public DatabaseItem(T item, DateTime insertDate, DateTime lastModified, boolean deleted, String insertId, String lastModifiedId) {
        this.item = item;
        this.insertDate = insertDate;
        this.lastModifiedDate = lastModified;
        this.deleted = deleted;
        this.insertId = insertId;
        this.lastModifiedId = lastModifiedId;
    }

    public DatabaseItem(T item, String insertId, String lastModifiedId) {
        this(item, DateTime.now(), DateTime.now(), insertId, lastModifiedId);
    }

    public DatabaseItem(T item, DateTime insertDate, DateTime lastModifiedDate, String insertId, String lastModifiedId) {
        this.item = item;
        this.insertDate = insertDate;
        this.lastModifiedDate = lastModifiedDate;
        this.deleted = false;
        this.insertId = insertId;
        this.lastModifiedId = lastModifiedId;
    }

    public DatabaseItem(T item, String id) {
        this(item, id, id);
    }

    public DatabaseItem(ByteBuffer buffer) {
        lastModifiedDate = byteToDateTime(buffer);
        insertDate = byteToDateTime(buffer);
        insertId = byteToString(buffer);
        lastModifiedId = byteToString(buffer);
        deleted = byteToBoolean(buffer);
        // Eventuell static create erstellen
        item = (T) byteToItem(buffer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "LGADatabaseItem{" +
                "deleted=" + deleted +
                ", id=" + item.getId() +
                ", insertDate=" + insertDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedId='" + lastModifiedId + '\'' +
                ", insertId='" + insertId + '\'' +
                '}';
    }

    public DateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(DateTime insertDate) {
        this.insertDate = insertDate;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getLastModifiedId() {
        return lastModifiedId;
    }

    public void setLastModifiedId(String lastModifiedId) {
        this.lastModifiedId = lastModifiedId;
    }

    public String getId() {
        return item.getId();
    }

    public T getItem() {
        return item;
    }

    public String getName() {
        return item.getName();
    }

    @Override
    public int getByteLength() {
        return getDateLength() * 2 + getStringLength(lastModifiedId) + getStringLength(insertId) + getBoolLength() + getItemLength(item);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        writeDateTime(lastModifiedDate, buffer);
        writeDateTime(insertDate, buffer);
        writeString(insertId, buffer);
        writeString(lastModifiedId, buffer);
        writeBool(deleted, buffer);
        writeItem(item, buffer);
    }
}
