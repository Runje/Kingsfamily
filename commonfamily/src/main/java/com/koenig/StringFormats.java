package com.koenig;

import com.koenig.commonModel.User;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by Thomas on 23.10.2017.
 */

public class StringFormats {

    public static final String dayFormat = "dd.MM.yy";
    private static final DecimalFormatSymbols symbols = buildSymbols();
    private static DecimalFormat centFormat = new DecimalFormat("0.00", symbols);
    private static DecimalFormat euroFormat = new DecimalFormat("0", symbols);
    private static DecimalFormat percentFormat = new DecimalFormat("0.#", symbols);

    private static DecimalFormatSymbols buildSymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        return symbols;
    }

    public static String dateTimeToDayString(DateTime dateTime) {
        return dateTime.toString(dayFormat);
    }

    public static String centsToCentString(int cents) {
        return StringFormats.centFormat.format(cents / 100.0);
    }

    public static String centsToEuroString(int cents) {
        return StringFormats.euroFormat.format(cents / 100.0);
    }

    public static String floatToPercentString(float percent) {
        return StringFormats.percentFormat.format(percent * 100.0);
    }


    public static String usersToAbbreviationString(List<User> users) {
        if (users.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (User user : users) {
            builder.append(user.getAbbreviation() + ",");
        }

        return builder.substring(0, builder.length() - 1);
    }
}
