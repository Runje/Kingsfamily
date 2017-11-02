package com.koenig.commonModel;


import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Thomas on 06.10.2017.
 */

public abstract class Item extends Byteable {
    private String id;

    public Item() {
        id = UUID.randomUUID().toString();
    }

    public Item(String id) {
        this.id = id;
    }

    public Item(ByteBuffer buffer) {
        id = byteToString(buffer);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getByteLength() {
        return getStringLength(id);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.put(stringToByte(id));
    }

}
