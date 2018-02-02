package com.koenig.commonModel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 06.10.2017.
 */

public class Family extends Item {
    private List<User> users;

    public Family(String name) {
        super(name);
        users = new ArrayList<>();
    }

    public Family(String name, List<User> users) {
        super(name);
        this.users = users;
    }

    public Family(ByteBuffer buffer) {
        super(buffer);
        short size = buffer.getShort();
        users = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            users.add(new User(buffer));
        }
    }

    public Family(String id, String name, List<User> users) {
        super(id, name);
        this.users = users;
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + Companion.getListLength(users);
    }


    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        Companion.writeList(users, buffer);
    }


    public List<User> getUsers() {
        return users;
    }
}
