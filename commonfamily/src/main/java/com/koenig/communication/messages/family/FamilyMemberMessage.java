package com.koenig.communication.messages.family;


import com.koenig.commonModel.Component;
import com.koenig.commonModel.User;
import com.koenig.communication.messages.FamilyMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2017.
 */

public class FamilyMemberMessage extends FamilyMessage {

    public static final String NAME = "FamilyMemberMessage";

    private List<User> members;

    public FamilyMemberMessage(List<User> members) {
        super(Component.FAMILY);
        this.members = members;
    }

    public FamilyMemberMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;

        int size = buffer.getShort();
        members = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            members.add(new User(buffer));
        }
    }

    public List<User> getMembers() {
        return members;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        int size = 0;
        for (User user
                : members) {
            size += user.getByteLength();
        }
        return 2 + size;
    }

    @Override
    protected byte[] contentToByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getContentLength());
        byteBuffer.putShort((short) members.size());
        for (User user : members) {
            user.writeBytes(byteBuffer);
        }

        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "FamilyMemberMessage{}";
    }
}
