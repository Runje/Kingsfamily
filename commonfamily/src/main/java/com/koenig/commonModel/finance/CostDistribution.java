package com.koenig.commonModel.finance;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.User;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 22.10.2017.
 */

public class CostDistribution extends Byteable {
    Map<User, Costs> distribution;

    public CostDistribution(Map<User, Costs> distribution) {
        this.distribution = distribution;
    }

    public CostDistribution() {
        distribution = new HashMap<>();
    }

    public CostDistribution(ByteBuffer buffer) {
        this();
        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            User user = new User(buffer);
            Costs costs = new Costs(buffer);
            distribution.put(user, costs);
        }
    }

    public Map<User, Costs> getDistribution() {
        return distribution;
    }

    public int sumReal() {
        int sumReal = 0;
        for (Costs c : distribution.values()) {
            sumReal += c.Real;
        }

        return sumReal;
    }

    public int sumTheory() {
        int sumTheory = 0;
        for (Costs c : distribution.values()) {
            sumTheory += c.Theory;
        }

        return sumTheory;
    }

    public boolean isValid() {
        return sumReal() == sumTheory();
    }

    public Costs getCostsFor(User user) {
        Costs costs = distribution.get(user);
        return costs == null ? new Costs(0, 0) : costs;
    }


    public void putCosts(User user, Costs costs) {
        distribution.put(user, costs);
    }

    public void putCosts(User user, int real, int theory) {
        putCosts(user, new Costs(real, theory));
    }

    @Override
    public String toString() {
        if (distribution.isEmpty()) {
            return "Empty CostDistribution";
        }
        StringBuilder builder = new StringBuilder();
        for (User user : distribution.keySet()) {
            Costs costs = distribution.get(user);
            builder.append(user.getName());
            builder.append(": ");
            builder.append(costs);
            builder.append("\n");
        }

        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public int getByteLength() {
        int size = 4;
        for (User user : distribution.keySet()) {
            size += user.getByteLength() + distribution.get(user).getByteLength();
        }

        return size;
    }

    public float getRealPercent(User user) {
        int sum = sumReal();
        if (sum == 0) {
            return 0;
        }

        return getCostsFor(user).Real / (float) sum;
    }

    public float getTheoryPercent(User user) {
        int sum = sumTheory();
        if (sum == 0) {
            return 0;
        }

        return getCostsFor(user).Theory / (float) sum;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.putInt(distribution.size());
        for (User user : distribution.keySet()) {
            user.writeBytes(buffer);
            buffer.put(distribution.get(user).getBytes());
        }
    }
}
