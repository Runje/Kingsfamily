package com.koenig.communication.messages.finance;


import com.koenig.commonModel.Category;
import com.koenig.commonModel.Component;
import com.koenig.communication.messages.FamilyMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2017.
 */

public class CategorysMessage extends FamilyMessage {

    public static final String NAME = "CategorysMessage";

    private List<Category> categorys;

    public CategorysMessage(List<Category> categorys) {
        super(Component.FINANCE);
        this.categorys = categorys;
    }

    public CategorysMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;

        int size = buffer.getShort();
        categorys = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            categorys.add(new Category(buffer));
        }
    }

    public List<Category> getCategorys() {
        return categorys;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        int size = 2;
        for (Category expenses : categorys) {
            size += expenses.getByteLength();
        }
        return size;
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        buffer.putShort((short) categorys.size());
        for (Category category : categorys) {
            category.writeBytes(buffer);
        }
    }

    @Override
    public String toString() {
        return "categoMessage{" +
                "TYPE_CATEGORYS=" + categorys +
                '}';
    }
}
