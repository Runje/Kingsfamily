package com.koenig;

/**
 * Created by Thomas on 19.11.2017.
 */

public class FamilyUtils {
    public static int getHalfRoundDown(int costsInCent) {
        return (int) Math.floor(costsInCent / 2.0);
    }

    public static int getHalfRoundUp(int costsInCent) {
        return (int) Math.ceil(costsInCent / 2.0);
    }
}
