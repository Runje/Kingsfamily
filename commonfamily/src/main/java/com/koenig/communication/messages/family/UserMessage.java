package com.koenig.communication.messages.family;

import com.koenig.commonModel.Component;
import com.koenig.commonModel.User;
import com.koenig.communication.messages.FamilyMessage;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.10.2017.
 */

public class UserMessage extends FamilyMessage {

    public static final String NAME = "UserMessage";

    private User user;


    public UserMessage(User user) {
        super(Component.FAMILY);
        this.user = user;
    }

    public UserMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.setVersion(version);
        this.setFromId(fromId);
        this.setToId(toId);
        user = new User(buffer);
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        return user.getByteLength();
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        user.writeBytes(byteBuffer);
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "user=" + user +
                '}';
    }
}
