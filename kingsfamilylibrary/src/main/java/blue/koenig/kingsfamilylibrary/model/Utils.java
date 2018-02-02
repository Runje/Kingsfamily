package blue.koenig.kingsfamilylibrary.model;

import android.content.Context;
import android.content.pm.PackageManager;

import com.koenig.FamilyConstants;

import org.joda.time.DateTime;

import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;

/**
 * Created by Thomas on 13.10.2017.
 */

public class Utils {

    public static String dateToString(DateTime dateTime) {
        if (dateTime.getMillis() == 0) {
            return FamilyConfig.INSTANCE.getNEVER();
        }

        return dateTime.toString(FamilyConfig.INSTANCE.getDATE_FORMAT());
    }

    public static DateTime stringToDateTime(String date) {
        if (date.equals(FamilyConfig.INSTANCE.getNEVER())) {
            return FamilyConstants.NO_DATE;
        }

        return DateTime.parse(date, FamilyConfig.INSTANCE.getDATE_FORMAT());
    }

    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static void setUserId(ServerConnection connection, Context context, String userId) {
        Installation.setId(context, userId);
        FamilyConfig.INSTANCE.saveUserId(userId, context);
        connection.setFromId(userId);
    }
}
