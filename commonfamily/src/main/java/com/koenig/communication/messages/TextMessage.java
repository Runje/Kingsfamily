package com.koenig.communication.messages;


import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class TextMessage extends FamilyMessage {

    public static final String NAME = "TextMessage";


    String text;

    public TextMessage(Component component, String text) {
        super(component);
        this.text = text;
    }

    public TextMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;
        this.text = Byteable.byteToString(buffer);
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return NAME;
    }

    @Override
    protected int getContentLength() {
        return Byteable.getStringLength(text);
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        buffer.put(Byteable.stringToByte(text));
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
