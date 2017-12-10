package blue.koenig.kingsfamilylibrary.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blue.koenig.kingsfamilylibrary.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyConfig {
    public static final String SHARED_PREF = "KINGSFAMILY_SHARED_PREF";
    public static final String SPECIFIC_PREF = "KINGSFAMILY_SPECIFIC_PREF";
    public static final String USERID = "USERID";
    public static final String NO_ID = "NO_ID";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY");
    public static final String NEVER = "-";
    private static final String LAST_SYNC_DATE = "LASTSYNCDATE";
    private static final long NO_DATE_LONG = 0;
    public static final DateTime NO_DATE = new DateTime(NO_DATE_LONG);
    private static Logger logger = LoggerFactory.getLogger("FamilyConfig");
    private static String userId = "";


    public static SharedPreferences getSharedPreferencesBetweenApps(Context context) {
        try {
            // TODO: what does ignore security really mean?
            Context familyContext = context.createPackageContext(context.getString(R.string.family_uri), Context.CONTEXT_IGNORE_SECURITY);
            return familyContext.getSharedPreferences(SHARED_PREF, MODE_WORLD_WRITEABLE);
        } catch (PackageManager.NameNotFoundException e) {
            logger.error("FAMILY app is not installed");

            // TODO: either the app must be installed or store userId in own shared preferences!?
            return null;
        }
    }
    /**
     * Gets the userId of the User.
     * @return Name of the User
     */
    public static String getUserId(Context context) {
        if (userId.equals(""))
        {
            SharedPreferences preferences = getSharedPreferencesBetweenApps(context);
            userId = preferences.getString(USERID, NO_ID);
        }

        return userId;
    }

    /**
     * Saves the userId to the shared preferences
     * @param userId
     * @param context
     */
    public static void saveUserId(String userId, Context context) {
        logger.info("Setting user id to " + userId);
        SharedPreferences preferences = getSharedPreferencesBetweenApps(context);
        preferences.edit().putString(USERID, userId).apply();
        FamilyConfig.userId = userId;
    }

    public static String getAbbreviationFor(String userName, Context context) {
        SharedPreferences preferences = getSharedPreferencesBetweenApps(context);
        return preferences.getString(userName, userName.substring(0, 1));
    }

    public static void setAbbreviationFor(String userName, String abbreviation, Context context) {
        SharedPreferences preferences = getSharedPreferencesBetweenApps(context);
        preferences.edit().putString(userName, abbreviation).apply();

    }

    /**
     * Gets the last sync date from shared prefs.
     *
     * @param context
     * @return
     */
    public static DateTime getLastSyncDate(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE);
        return new DateTime(preferences.getLong(LAST_SYNC_DATE + name, NO_DATE_LONG));
    }

    /**
     * Saves the last sync date to shared prefs.
     *
     * @param date
     * @param context
     */
    public static void saveLastSyncDate(DateTime date, Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(SPECIFIC_PREF, MODE_PRIVATE);
        preferences.edit().putLong(LAST_SYNC_DATE + name, date.getMillis()).commit();
    }

}
