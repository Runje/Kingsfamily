package com.koenig.communication.messages;

import com.koenig.commonModel.Component;
import com.koenig.commonModel.Item;
import com.koenig.commonModel.Operation;
import com.koenig.commonModel.Operator;
import com.koenig.commonModel.finance.Expenses;

import java.nio.ByteBuffer;

import static com.koenig.commonModel.Operator.ADD;
import static com.koenig.commonModel.Operator.DELETE;
import static com.koenig.commonModel.Operator.UPDATE;

/**
 * Created by Thomas on 02.11.2017.
 */

public class AUDMessage extends FamilyMessage {
    public static final String NAME = "AUDMessage";
    Operation operation;

    public AUDMessage(Component component, Operation operation) {
        super(component);
        this.operation = operation;
    }

    public AUDMessage(int version, Component component, String fromId, String toId, Operation operation) {
        super(version, component, fromId, toId);
        this.operation = operation;
    }

    public AUDMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;
        operation = new Operation(buffer);
    }

    private static AUDMessage create(Operator operation, Item item) {
        Component component;
        if (item instanceof Expenses || item instanceof Operation) {
            component = Component.FINANCE;
            return new AUDMessage(component, new Operation(operation, item));
        } else {
            throw new RuntimeException("Unsupported item: " + item.toString());
        }
    }

    public static AUDMessage createAdd(Item item) {
        return create(ADD, item);
    }

    public static AUDMessage createUpdate(Item item) {
        return create(UPDATE, item);
    }

    public static AUDMessage createDelete(Item item) {
        return create(DELETE, item);
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected int getContentLength() {
        return operation.getByteLength();
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        operation.writeBytes(buffer);
    }
}
