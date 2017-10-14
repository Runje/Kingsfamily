package com.koenig.communication.messages;

import com.koenig.BinaryConverter;
import com.koenig.commonModel.User;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.10.2017.
 */

public class UserMessage extends FamilyMessage {

    public static final String NAME = "UserMessage";

    private User user;


    public UserMessage(User user) {
        this.user = user;
    }

    public UserMessage(String fromId, String toId, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        user = BinaryConverter.byteToUser(buffer);
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        return BinaryConverter.getUserLength(user);
    }

    @Override
    protected byte[] contentToByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getContentLength());
        byteBuffer.put(BinaryConverter.userToBytes(user));
        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "user=" + user +
                '}';
    }
}
