package com.koenig.communication.messages;


import com.koenig.commonModel.Component;
import com.koenig.commonModel.Item;
import com.koenig.commonModel.database.DatabaseItem;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2017.
 */

public class UpdatesMessage<T extends Item> extends FamilyMessage {

    public static final String NAME = "UpdatesMessage";
    private List<DatabaseItem<T>> items;

    public UpdatesMessage(List<DatabaseItem<T>> items) {
        super(Component.FINANCE);
        this.items = items;
    }

    public UpdatesMessage(int version, Component component, String fromId, String toId, List<DatabaseItem<T>> items) {
        super(component);
        this.setVersion(version);
        this.setFromId(fromId);
        this.setToId(toId);
        this.items = items;
    }

    public UpdatesMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.setVersion(version);
        this.setFromId(fromId);
        this.setToId(toId);
        int size = buffer.getShort();
        items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(new DatabaseItem<T>(buffer));
        }
    }


    public List<DatabaseItem<T>> getItems() {
        return items;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        int size = 2;
        for (DatabaseItem<T> item : items) {
            size += item.getByteLength();
        }
        return size;
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        buffer.putShort((short) items.size());
        for (DatabaseItem<T> item : items) {
            item.writeBytes(buffer);
        }
    }

    @Override
    public String toString() {
        return "UpdatesMessage{" +
                "items=" + items +
                '}';
    }
}
