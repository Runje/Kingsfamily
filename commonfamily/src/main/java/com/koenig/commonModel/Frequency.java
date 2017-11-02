package com.koenig.commonModel;

/**
 * Created by Thomas on 06.09.2015.
 */
public enum Frequency {
    Daily, Weekly, Monthly, Yearly;

    public static Frequency indexToFrequency(int idx) {
        Frequency frequency = null;
        if (idx == 0) {
            frequency = Frequency.Daily;
        } else if (idx == 1) {
            frequency = Frequency.Weekly;
        } else if (idx == 2) {
            frequency = Frequency.Monthly;
        } else if (idx == 3) {
            frequency = Frequency.Yearly;
        }

        return frequency;
    }

    public static int FrequencyToIndex(Frequency frequency) {
        switch (frequency) {
            case Daily:
                return 0;
            case Weekly:
                return 1;
            case Monthly:
                return 2;
            case Yearly:
                return 3;
        }

        return -1;
    }


}
