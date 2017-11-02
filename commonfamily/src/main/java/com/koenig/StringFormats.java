package com.koenig;

import java.text.DecimalFormat;

/**
 * Created by Thomas on 23.10.2017.
 */

public class StringFormats {

    public static final DecimalFormat centFormat = new DecimalFormat("0.00");
    public static final DecimalFormat euroFormat = new DecimalFormat("0");

    public static String centsToCentString(int cents) {
        return StringFormats.centFormat.format(cents / 100.0);
    }

    public static String centsToEuroString(int cents) {
        return StringFormats.euroFormat.format(cents / 100.0);
    }
}
