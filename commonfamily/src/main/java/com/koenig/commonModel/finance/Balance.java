package com.koenig.commonModel.finance;

import com.koenig.commonModel.Item;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

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
        date = byteToDateTime(buffer);
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
        return super.getByteLength() + 4 + getDateLength();
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        buffer.putInt(balance);
        writeDateTime(date, buffer);
    }
}