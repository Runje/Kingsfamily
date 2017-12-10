package com.koenig.commonModel;


import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Thomas on 06.10.2017.
 */

public abstract class Item extends Byteable {
    protected String id;
    protected String name;

    public Item(String name) {
        this.name = name;
        id = UUID.randomUUID().toString();
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(ByteBuffer buffer) {
        id = byteToString(buffer);
        name = byteToString(buffer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getByteLength() {
        return getStringLength(id) + getStringLength(name);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.put(stringToByte(id));
        writeString(name, buffer);
    }

}
