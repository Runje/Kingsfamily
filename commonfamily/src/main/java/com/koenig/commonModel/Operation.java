package com.koenig.commonModel;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 21.11.2017.
 */

public class Operation<T extends Item> extends Item {


    private Operator operator;
    private T item;


    public Operation(String id, Operator operator, T item) {
        super(id, item.getName());
        this.operator = operator;
        this.item = item;
    }

    public Operation(Operator operator, T item) {
        super(item.getName());
        this.operator = operator;
        this.item = item;
    }

    public Operation(ByteBuffer buffer) {
        super(buffer);
        operator = Companion.byteToEnum(buffer, Operator.class);
        item = (T) Companion.byteToItem(buffer);
    }

    public static Operation createAdd(Item item) {
        return new Operation(Operator.ADD, item);
    }

    public static Operation createUpdate(Item item) {
        return new Operation(Operator.UPDATE, item);
    }

    public static Operation createDelete(Item item) {
        return new Operation(Operator.DELETE, item);
    }

    public Operator getOperator() {
        return operator;
    }

    public T getItem() {
        return item;
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + Byteable.Companion.getEnumLength(operator) + Byteable.Companion.getItemLength(item);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        Byteable.Companion.writeEnum(operator, buffer);
        Byteable.Companion.writeItem(item, buffer);
    }
}
