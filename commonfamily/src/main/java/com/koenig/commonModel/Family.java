package com.koenig.commonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 06.10.2017.
 */

public class Family extends Item {
    private String name;
    private List<User> users;

    public Family() {
        users = new ArrayList<>();
    }

    public Family(String name) {
        this();
        this.name = name;
    }

    public Family(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }
}
