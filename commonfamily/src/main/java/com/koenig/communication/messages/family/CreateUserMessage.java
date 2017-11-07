package com.koenig.communication.messages.family;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;
import com.koenig.communication.messages.FamilyMessage;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.10.2017.
 */

public class CreateUserMessage extends FamilyMessage {

    public static final String NAME = "CreateUserMessage";

    private String userName;
    private DateTime birthday;

    public CreateUserMessage(String userName, DateTime birthday) {
        super(Component.FAMILY);
        this.birthday = birthday;
        this.userName = userName;
    }

    public CreateUserMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(version, component, fromId, toId);
        userName = Byteable.byteToString(buffer);
        birthday = Byteable.byteToDateTime(buffer);
    }

    public String getUserName() {
        return userName;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        return Byteable.getStringLength(userName) + Byteable.getDateLength();
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        buffer.put(Byteable.stringToByte(userName));
        buffer.put(Byteable.dateTimeToBytes(birthday));
    }

    @Override
    public String toString() {
        return "CreateUserMessage{" +
                "userName='" + userName + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
