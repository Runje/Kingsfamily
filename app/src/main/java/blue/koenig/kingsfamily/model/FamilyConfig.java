package blue.koenig.kingsfamily.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyConfig {
    public static final String SHARED_PREF = "KINGSFAMILY_SHARED_PREF";
    public static final String USERNAME = "USERNAME";
    public static final String FAMILYNAME = "FAMILYNAME";
    public static final String NO_NAME = "NO_NAME";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY");
    public static final String NEVER = "-";
    private static final String LAST_SYNC_DATE = "LASTSYNCDATE";
    private static final long NO_DATE_LONG = 0;
    public static final DateTime NO_DATE = new DateTime(NO_DATE_LONG);
    private static String username = "";
    private static String familyname = "";

    /**
     * Gets the username of the User.
     * @return Name of the User
     */
    public static String getUsername(Context context) {
        if (username.equals(""))
        {
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            username = preferences.getString(USERNAME, NO_NAME);
        }

        return username;
    }

    /**
     * Saves the nameToSave to the shared preferences
     * @param nameToSave
     * @param context
     */
    public static void saveUsername(String nameToSave, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        preferences.edit().putString(USERNAME, nameToSave).apply();
        username = nameToSave;
    }

    /**
     * Gets the family username of the User.
     * @return Family Name of the User
     */
    public static String getFamilyname(Context context) {
        if (familyname.equals(""))
        {
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            familyname = preferences.getString(FAMILYNAME, NO_NAME);
        }

        return familyname;
    }

    /**
     * Saves the nameToSave to the shared preferences
     * @param nameToSave
     * @param context
     */
    public static void saveFamilyname(String nameToSave, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        preferences.edit().putString(FAMILYNAME, nameToSave).commit();
        familyname = nameToSave;
    }

}
