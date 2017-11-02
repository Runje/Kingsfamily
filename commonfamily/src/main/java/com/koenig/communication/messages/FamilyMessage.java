package com.koenig.communication.messages;



import com.example.Message;
import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public abstract class FamilyMessage implements Message {
    public static final String ServerId = "KOENIGSPUTZ_SERVER_ID";
    public static final String SEPARATOR = ";";
    private static final int VERSION_NUMBER = 1;
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected String fromId;
    protected String toId;
    protected Component component;
    protected int version;

    public FamilyMessage(Component component) {
        this.component = component;
        this.version = VERSION_NUMBER;
    }



    public abstract String getName();

    public int getTotalLength()
    {
        int length = 4 + 4 + component.getBytesLength() + Byteable.getStringLength(getName()) + Byteable.getStringLength(fromId) + Byteable.getStringLength(toId)
                + getContentLength();
        return length;
    }

    protected abstract int getContentLength();

    protected abstract byte[] contentToByte();

    public String getFromId()
    {
        return fromId;
    }

    public void setFromId(String fromId)
    {
        this.fromId = fromId;
    }

    public Component getComponent() {
        return component;
    }

    public int getVersion() {
        return version;
    }

    public String getToId()
    {
        return toId;
    }

    public void setToId(String toId)
    {
        this.toId = toId;
    }

    protected void headerToBuffer(ByteBuffer buffer) {
        buffer.putInt(getTotalLength());
        buffer.putInt(version);
        buffer.put(component.toBytes());
        buffer.put(Byteable.stringToByte(getName()));
        buffer.put(Byteable.stringToByte(fromId));
        buffer.put(Byteable.stringToByte(toId));
    }

    public ByteBuffer getBuffer()
    {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());

        headerToBuffer(buffer);
        buffer.put(contentToByte());
        buffer.flip();
        return buffer;
    }
}
