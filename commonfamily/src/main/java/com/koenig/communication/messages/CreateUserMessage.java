package com.koenig.communication.messages;

import com.koenig.BinaryConverter;

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
        this.birthday = birthday;
        this.userName = userName;
    }

    public CreateUserMessage(String fromId, String toId, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        userName = BinaryConverter.byteToString(buffer);
        birthday = BinaryConverter.getDateTime(buffer);
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
        return BinaryConverter.stringLengthSize + userName.length() + BinaryConverter.dateTimeLength;
    }

    @Override
    protected byte[] contentToByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getContentLength());
        byteBuffer.put(BinaryConverter.stringToByte(userName));
        byteBuffer.put(BinaryConverter.dateTimeToBytes(birthday));
        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "CreateUserMessage{" +
                "userName='" + userName + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
