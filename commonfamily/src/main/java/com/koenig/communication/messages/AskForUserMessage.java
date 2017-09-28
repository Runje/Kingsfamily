package com.koenig.communication.messages;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class AskForUserMessage extends FamilyMessage {

    public static final String NAME = "AskForUserMessage";

    public AskForUserMessage() {
    }

    public AskForUserMessage(String fromId, String toId, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
    }


    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        return 0;
    }

    @Override
    protected byte[] contentToByte() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "AskForUserMessage{}";
    }
}
