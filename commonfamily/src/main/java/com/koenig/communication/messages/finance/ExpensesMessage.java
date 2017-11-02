package com.koenig.communication.messages.finance;


import com.koenig.commonModel.Component;
import com.koenig.commonModel.finance.Expenses;
import com.koenig.communication.messages.FamilyMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2017.
 */

public class ExpensesMessage extends FamilyMessage {

    public static final String NAME = "ExpensesMessage";

    private List<Expenses> expenses;

    public ExpensesMessage(List<Expenses> expenses) {
        super(Component.FINANCE);
        this.expenses = expenses;
    }

    public ExpensesMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;

        int size = buffer.getShort();
        expenses = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            expenses.add(new Expenses(buffer));
        }
    }

    public List<Expenses> getExpenses() {
        return expenses;
    }

    public String getName() {
        return NAME;
    }


    @Override
    protected int getContentLength() {
        int size = 2;
        for (Expenses expenses : expenses) {
            size += expenses.getByteLength();
        }
        return size;
    }

    @Override
    protected byte[] contentToByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getContentLength());
        byteBuffer.putShort((short) expenses.size());
        for (Expenses expenses : expenses) {
            expenses.writeBytes(byteBuffer);
        }

        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "ExpensesMessage{" +
                "expenses=" + expenses +
                '}';
    }
}
