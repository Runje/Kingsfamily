package com.koenig.commonModel.finance;

import com.koenig.commonModel.Frequency;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Thomas on 22.10.2017.
 */

public class StandingOrder extends BookkeepingEntry {
    List<String> executedExpenses;
    private DateTime firstDate;
    private DateTime endDate;
    private Frequency frequency;
    private int frequencyFactor;

    public StandingOrder(String name, String category, String subCategory, int costs, CostDistribution costDistribution, DateTime firstDate, DateTime endDate, Frequency frequency, int frequencyFactor, List<String> executedExpenses) {
        super(name, category, subCategory, costs, costDistribution);
        this.firstDate = firstDate;
        this.frequency = frequency;
        this.frequencyFactor = frequencyFactor;
        this.executedExpenses = executedExpenses;
        this.endDate = endDate;
    }

    public StandingOrder(BookkeepingEntry entry, DateTime firstDate, DateTime endDate, Frequency frequency, int frequencyFactor, List<String> executedExpenses) {
        super(entry);
        this.firstDate = firstDate;
        this.frequency = frequency;
        this.frequencyFactor = frequencyFactor;
        this.executedExpenses = executedExpenses;
        this.endDate = endDate;
    }

    public DateTime getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(DateTime firstDate) {
        this.firstDate = firstDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyFactor() {
        return frequencyFactor;
    }

    public void setFrequencyFactor(int frequencyFactor) {
        this.frequencyFactor = frequencyFactor;
    }

    public List<String> getExecutedExpenses() {
        return executedExpenses;
    }

    public void setExecutedExpenses(List<String> executedExpenses) {
        this.executedExpenses = executedExpenses;
    }

    public void addExpenses(String expensesId) {
        executedExpenses.add(expensesId);
    }

    @Override
    public String toString() {
        return "StandingOrder{" + super.toString() +
                "firstDate=" + firstDate +
                ", endDate=" + endDate +
                ", frequency=" + frequency +
                ", frequencyFactor=" + frequencyFactor +
                ", executedExpenses=" + executedExpenses +
                '}';
    }
}
