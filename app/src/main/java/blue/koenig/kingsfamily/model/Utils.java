package blue.koenig.kingsfamily.model;

import org.joda.time.DateTime;

/**
 * Created by Thomas on 13.10.2017.
 */

public class Utils {

    public static String dateToString(DateTime dateTime) {
        if (dateTime.getMillis() == 0) {
            return FamilyConfig.NEVER;
        }

        return dateTime.toString(FamilyConfig.DATE_FORMAT);
    }

    public static DateTime stringToDateTime(String date) {
        if (date.equals(FamilyConfig.NEVER)) {
            return new FamilyConfig().NO_DATE;
        }

        return DateTime.parse(date, FamilyConfig.DATE_FORMAT);
    }
}
