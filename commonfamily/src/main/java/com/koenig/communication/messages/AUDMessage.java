package com.koenig.communication.messages;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;
import com.koenig.commonModel.finance.Expenses;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 02.11.2017.
 */

public class AUDMessage<T extends Byteable> extends FamilyMessage {
    public static final String NAME = "AUDMessage";
    public final static String TYPE_EXPENSES = "Expenses";
    public final static String OPERATION_ADD = "add";
    public final static String OPERATION_DELETE = "delete";
    public final static String OPERATION_UPDATE = "udpate";
    private String type;
    private String operation;
    private T item;

    public AUDMessage(Component component, String operation, T item) {
        super(component);
        this.type = itemToType(item);
        this.operation = operation;
        this.item = item;
    }

    public AUDMessage(int version, Component component, String fromId, String toId, String operation, T item) {
        super(version, component, fromId, toId);
        this.operation = operation;
        this.item = item;
    }

    private static String itemToType(Byteable item) {
        if (item instanceof Expenses) {
            return TYPE_EXPENSES;
        } else {
            throw new RuntimeException("Unsupported item: " + item.toString());
        }
    }

    public static AUDMessage create(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        String type = Byteable.byteToString(buffer);
        String operation = Byteable.byteToString(buffer);
        if (type.equals(TYPE_EXPENSES)) {
            return new AUDMessage<Expenses>(version, component, fromId, toId, operation, new Expenses(buffer));
        } else {
            throw new RuntimeException("Unsupported item: " + type.toString());
        }
    }

    private static AUDMessage create(String operation, Byteable item) {
        Component component;
        if (item instanceof Expenses) {
            component = Component.FINANCE;
            return new AUDMessage(component, operation, item);
        } else {
            throw new RuntimeException("Unsupported item: " + item.toString());
        }
    }

    public static AUDMessage createAdd(Byteable item) {
        return create(OPERATION_ADD, item);
    }

    public static AUDMessage createUpdate(Byteable item) {
        return create(OPERATION_UPDATE, item);
    }

    public static AUDMessage createDelete(Byteable item) {
        return create(OPERATION_DELETE, item);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected int getContentLength() {
        return Byteable.getStringLength(type) + Byteable.getStringLength(operation) + item.getByteLength();
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        Byteable.writeString(type, buffer);
        Byteable.writeString(operation, buffer);
        item.writeBytes(buffer);
    }

    public String getType() {
        return type;
    }

    public String getOperation() {
        return operation;
    }

    public T getItem() {
        return item;
    }
}
