package com.koenig.communication.messages;



import com.example.Message;
import com.koenig.BinaryConverter;
import com.koenig.communication.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public abstract class FamilyMessage implements Message {
    public static final String ServerId = "KOENIGSPUTZ_SERVER_ID";
    public static final String SEPARATOR = ";";
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected String fromId;
    protected String toId;

    public static FamilyMessage CreateFamilyMessage(String familyName, String userName) {
        return new TextMessage(Commands.CREATE_FAMILY + SEPARATOR + familyName + SEPARATOR + userName);
    }

    public abstract String getName();

    public int getTotalLength()
    {
        int length = BinaryConverter.sizeLength + 2 + getName().length() + 2 + fromId.length() + 2 + toId.length() + getContentLength();
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
        buffer.put(BinaryConverter.stringToByte(getName()));
        buffer.put(BinaryConverter.stringToByte(fromId));
        buffer.put(BinaryConverter.stringToByte(toId));
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
