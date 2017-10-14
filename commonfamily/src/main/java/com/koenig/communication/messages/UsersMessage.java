package com.koenig.communication.messages;


import com.koenig.BinaryConverter;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class UsersMessage extends FamilyMessage {

    public static final String NAME = "UsersMessage";

    private String[] names;

    public UsersMessage(String[] names) {
        this.names = names;
    }

    public UsersMessage(String fromId, String toId, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;

        int size = buffer.getShort();
        names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = BinaryConverter.byteToString(buffer);
        }
    }

    public String[] getNames() {
        return names;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        int size = 0;
        for (String name : names) {
            size += name.length() + BinaryConverter.stringLengthSize;
        }
        return 2 + size;
    }

    @Override
    protected byte[] contentToByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getContentLength());
        byteBuffer.putShort((short) names.length);
        for (String name : names) {
            byteBuffer.put(BinaryConverter.stringToByte(name));
        }

        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "UsersMessage{}";
    }
}
