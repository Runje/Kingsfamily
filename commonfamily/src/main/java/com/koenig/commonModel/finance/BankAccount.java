package com.koenig.commonModel.finance;

import com.koenig.commonModel.Item;
import com.koenig.commonModel.User;

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
    private List<Balance> balances;

    public BankAccount(String id, String name, String bank, List<User> owners, List<Balance> balances) {
        super(id, name);
        this.owners = owners;
        this.name = name;
        this.bank = bank;
        this.balances = balances;
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

    public static void sortBalances(List<Balance> balances) {
        Collections.sort(balances, (lhs, rhs) -> rhs.getDate().compareTo(lhs.getDate()));
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
        return super.getByteLength() + getListLength(owners) + getStringLength(name) + getStringLength(bank) + getListLength(balances);
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        writeList(owners, buffer);
        writeString(name, buffer);
        writeString(bank, buffer);
        writeList(balances, buffer);
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
}
