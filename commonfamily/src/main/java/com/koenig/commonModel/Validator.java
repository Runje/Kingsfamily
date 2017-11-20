package com.koenig.commonModel;

import org.joda.time.DateTime;

/**
 * Created by Thomas on 12.11.2017.
 */

public class Validator {

    public static boolean isNotEmpty(String string) {
        return !string.trim().isEmpty();
    }

    public static boolean dateIsReasonable(DateTime date) {
        return date.getYear() > 1900 && date.getYear() < 2200;
    }

    public static boolean isEmptyOrId(String standingOrder) {
        return standingOrder.isEmpty() || isId(standingOrder);
    }

    private static boolean isId(String standingOrder) {
        // example: "c572d4e7-da4b-41d8-9c1f-7e9a97657155";
        // TODO: check position of "-"
        return standingOrder.length() == 36;
    }
}
