package com.koenig.commonModel.finance;

import com.koenig.commonModel.Byteable;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 22.10.2017.
 */

public class CostDistribution extends Byteable {
    Map<String, Costs> distribution;

    public CostDistribution(Map<String, Costs> distribution) {
        this.distribution = distribution;
    }

    public CostDistribution() {
        distribution = new HashMap<>();
    }

    public CostDistribution(ByteBuffer buffer) {
        this();
        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            String id = byteToString(buffer);
            Costs costs = new Costs(buffer);
            distribution.put(id, costs);
        }
    }

    public Map<String, Costs> getDistribution() {
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

    public Costs getCostsFor(String userId) {
        Costs costs = distribution.get(userId);
        return costs == null ? new Costs(0, 0) : costs;
    }

    public void putCosts(String userId, Costs costs) {
        distribution.put(userId, costs);
    }

    public void putCosts(String userId, int real, int theory) {
        putCosts(userId, new Costs(real, theory));
    }

    @Override
    public String toString() {
        if (distribution.isEmpty()) {
            return "Empty CostDistribution";
        }
        StringBuilder builder = new StringBuilder();
        for (String userId : distribution.keySet()) {
            Costs costs = distribution.get(userId);
            builder.append(userId);
            builder.append(": ");
            builder.append(costs);
            builder.append(System.lineSeparator());
        }

        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public int getByteLength() {
        int size = 4;
        for (String id : distribution.keySet()) {
            size += stringLengthSize + id.length() + distribution.get(id).getByteLength();
        }

        return size;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.putInt(distribution.size());
        for (String id : distribution.keySet()) {
            buffer.put(stringToByte(id));
            buffer.put(distribution.get(id).getBytes());
        }
    }
}
