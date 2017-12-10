package com.koenig.commonModel.finance;

import com.koenig.commonModel.Item;
import com.koenig.commonModel.Validator;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 21.10.2017.
 */

public class BookkeepingEntry extends Item {

    // userId --> costs
    public CostDistribution costDistribution;
    private int costs;
    private String category;
    private String subCategory;


    public BookkeepingEntry(String name, String category, String subCategory, int costs, CostDistribution costDistribution) {
        super(name);
        this.costs = costs;
        this.category = category;
        this.subCategory = subCategory;
        this.costDistribution = costDistribution;
    }

    public BookkeepingEntry(BookkeepingEntry entry) {
        super(entry.getName());
        this.costs = entry.costs;
        this.category = entry.category;
        this.subCategory = entry.subCategory;
        this.costDistribution = entry.costDistribution;
    }

    public BookkeepingEntry(ByteBuffer buffer) {
        super(buffer);
        category = byteToString(buffer);
        subCategory = byteToString(buffer);
        costs = buffer.getInt();
        costDistribution = new CostDistribution(buffer);
    }


    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public CostDistribution getCostDistribution() {
        return costDistribution;
    }

    public void setCostDistribution(CostDistribution costDistribution) {
        this.costDistribution = costDistribution;
    }

    public boolean costsValid() {
        return costDistribution.sumReal() == costs;
    }

    @Override
    public int getByteLength() {
        return super.getByteLength() + getStringLength(category) + getStringLength(subCategory) + 4 + costDistribution.getByteLength();
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        buffer.put(stringToByte(category));
        buffer.put(stringToByte(subCategory));
        buffer.putInt(costs);
        costDistribution.writeBytes(buffer);
    }

    public boolean isValid() {
        // subcategory is allowed to be empty
        return Validator.isNotEmpty(name) && Validator.isNotEmpty(category) &&
                costDistribution.isValid() && costDistribution.sumReal() == costs;
    }

    @Override
    public String toString() {
        return "BookkeepingEntry{" +
                "costs=" + costs +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", costDistribution=" + costDistribution +
                '}';
    }
}
