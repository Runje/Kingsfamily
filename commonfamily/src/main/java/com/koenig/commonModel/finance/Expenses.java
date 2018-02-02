package com.koenig.commonModel.finance;

import com.koenig.commonModel.Validator;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 21.10.2017.
 */

public class Expenses extends BookkeepingEntry {

    private DateTime date;
    // id of executing standingOrder
    private String standingOrder;

    public Expenses(String name, String category, String subCategory, int costs, CostDistribution costDistribution, DateTime date, String standingOrderId) {
        super(name, category, subCategory, costs, costDistribution);
        this.date = date;
        this.standingOrder = standingOrderId;
    }

    public Expenses(BookkeepingEntry entry, DateTime date, String standingOrderId) {
        super(entry);
        this.date = date;
        this.standingOrder = standingOrderId;
    }

    public Expenses(ByteBuffer buffer) {
        super(buffer);
        date = Companion.byteToDateTime(buffer);
        standingOrder = Companion.byteToString(buffer);
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getStandingOrder() {
        return standingOrder;
    }

    public void setStandingOrder(String standingOrder) {
        this.standingOrder = standingOrder;
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + Companion.getDateLength() + Companion.getStringLength(standingOrder);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        buffer.put(Companion.dateTimeToBytes(date));
        buffer.put(Companion.stringToByte(standingOrder));
    }

    @Override
    public String toString() {
        return "Expenses{" + super.toString() +
                "date=" + date +
                ", standingOrder='" + standingOrder + '\'' +
                '}';
    }

    public boolean isValid() {
        return super.isValid() && Validator.dateIsReasonable(date) && Validator.isEmptyOrId(standingOrder);
    }
}
