package com.koenig.communication.messages;


import com.koenig.communication.MessageConverter;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class TextMessage extends FamilyMessage {

    public static final String NAME = "TextMessage";


    String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public TextMessage(String fromId, String toId, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        this.text = MessageConverter.byteToString(buffer);
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return NAME;
    }

    @Override
    protected int getContentLength() {
        return text.length() + MessageConverter.stringLengthSize;
    }

    @Override
    protected byte[] contentToByte() {

        ByteBuffer buffer = ByteBuffer.allocate(getContentLength());
        buffer.put(MessageConverter.stringToByte(text));
        return buffer.array();
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
