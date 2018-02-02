package com.koenig.commonModel.finance;

import com.koenig.commonModel.Item;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 23.12.2017.
 */

public class Balance extends Item {
    private int balance;
    private DateTime date;

    public Balance(String id, int balance, DateTime dateTime) {
        super(id, Integer.toString(balance));
        this.balance = balance;
        this.date = dateTime;
    }

    public Balance(int balance, DateTime dateTime) {
        super(Integer.toString(balance));
        this.balance = balance;
        this.date = dateTime;
    }

    public Balance(ByteBuffer buffer) {
        super(buffer);
        balance = buffer.getInt();
        date = Companion.byteToDateTime(buffer);
    }

    public static List<Balance> getBalances(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int size = buffer.getInt();
        ArrayList<Balance> balances = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            balances.add(new Balance(buffer));
        }

        return balances;
    }

    public static byte[] listToBytes(List<Balance> balances) {
        int size = 4;
        for (Balance balance : balances) {
            size += balance.getByteLength();
        }

        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(balances.size());
        for (Balance balance : balances) {
            balance.writeBytes(buffer);
        }
        return buffer.array();
    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + 4 + Companion.getDateLength();
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        buffer.putInt(balance);
        Companion.writeDateTime(date, buffer);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                ", date=" + date +
                '}';
    }
}
