package com.koenig.commonModel;

import java.util.UUID;

/**
 * Created by Thomas on 06.10.2017.
 */

public abstract class Item {
    private String id;

    public Item() {
        id = UUID.randomUUID().toString();
    }

    public Item(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
