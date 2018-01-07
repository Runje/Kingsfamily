package com.koenig.commonModel.finance;

import com.koenig.FamilyConstants;
import com.koenig.commonModel.Item;
import com.koenig.commonModel.User;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Thomas on 21.10.2015.
 */
public class BankAccount extends Item {
    private List<User> owners;
    private String bank;
    /**
     * Sorted list where the first one is the newest
     */
    private List<Balance> balances;

    public BankAccount(String id, String name, String bank, List<User> owners, List<Balance> balances) {
        super(id, name);
        this.owners = owners;
        this.name = name;
        this.bank = bank;
        this.balances = balances;
        sortBalances(balances);
    }

    public BankAccount(String id, String name, String bank, User owner, List<Balance> balances) {
        super(id, name);
        this.owners = new ArrayList<>(1);
        owners.add(owner);
        this.name = name;
        this.bank = bank;
        this.balances = balances;
        sortBalances(balances);
    }

    public BankAccount(String name, String bank, List<User> owners, List<Balance> balances) {
        super(name);
        this.owners = owners;
        this.name = name;
        this.bank = bank;
        this.balances = balances;
        sortBalances(balances);
    }

    public BankAccount(ByteBuffer buffer) {
        super(buffer);
        short size = buffer.getShort();
        this.owners = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            owners.add(new User(buffer));
        }

        bank = byteToString(buffer);
        size = buffer.getShort();
        balances = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            balances.add(new Balance(buffer));
        }
    }

    public static void sortBalances(List<Balance> balances) {
        // newest at top
        Collections.sort(balances, (lhs, rhs) -> rhs.getDate().compareTo(lhs.getDate()));
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        writeList(owners, buffer);
        writeString(bank, buffer);
        writeList(balances, buffer);
    }

    public DateTime getDateTime() {
        if (balances.size() == 0) {
            return FamilyConstants.NO_DATE;
        }

        return balances.get(0).getDate();
    }

    public void addBalance(Balance balance) {
        balances.add(balance);
        sortBalances(balances);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owners=" + owners +
                ", bank='" + bank + '\'' +
                ", balances=" + balances +
                '}';
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + getListLength(owners) + getStringLength(bank) + getListLength(balances);
    }



    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getBalance() {
        if (balances.size() == 0) return 0;

        return balances.get(0).getBalance();
    }

    public String toReadableString() {
        return getBank() + " - " + getName();
    }

    public void deleteBalance(Balance balance) {
        Balance removeBalance = null;
        for (Balance bal : balances) {
            if (bal.getDate().equals(balance.getDate())) {
                removeBalance = bal;
                break;
            }
        }

        balances.remove(removeBalance);
    }
}
