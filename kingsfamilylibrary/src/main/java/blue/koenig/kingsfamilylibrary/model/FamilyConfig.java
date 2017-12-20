package blue.koenig.kingsfamilylibrary.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.User;

import net.iharder.Base64;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import blue.koenig.kingsfamilylibrary.model.shared.FamilyContentProvider;

import static android.content.Context.MODE_PRIVATE;
import static com.koenig.FamilyConstants.NO_DATE_LONG;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyConfig {
    public static final String SHARED_PREF = "KINGSFAMILY_SHARED_PREF";
    public static final String SPECIFIC_PREF = "KINGSFAMILY_SPECIFIC_PREF";
    public static final String USERID = "USERID";
    public static final String NO_ID = "";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY");
    public static final String NEVER = "-";
    private static final String LAST_SYNC_DATE = "LASTSYNCDATE";


    private static final String FAMILY_MEMBERS = "FAMILY_MEMBERS";
    private static final String FAMILY_NAME = "FAMILY_NAME";
    private static final String USER_NAME = "USER_NAME";
    private static Logger logger = LoggerFactory.getLogger("FamilyConfig");
    private static String userId = NO_ID;



    /**
     * Gets the userId of the User.
     * @return Name of the User
     */
    public static String getUserId(Context context) {
        if (userId.equals(NO_ID))
        {
            String id = FamilyContentProvider.get(context, USERID);
            userId = id;
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
        FamilyContentProvider.put(context, USERID, userId);
        FamilyConfig.userId = userId;
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

    public static List<User> getFamilyMembers(Context context) {
        String membersString = FamilyContentProvider.get(context, FAMILY_MEMBERS);
        if (membersString.isEmpty()) {
            return new ArrayList<>();
        }

        ByteBuffer buffer = null;
        try {
            buffer = ByteBuffer.wrap(Base64.decode(membersString));
            short size = buffer.getShort();
            List<User> members = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                members.add(new User(buffer));
            }
            return members;
        } catch (IOException e) {
            logger.error("Error decoding family members");
            return new ArrayList<>();
        }
    }

    public static void setFamilyMembers(Context context, List<User> members) {
        ByteBuffer buffer = ByteBuffer.allocate(Byteable.getListLength(members));
        Byteable.writeList(members, buffer);
        String membersString = Base64.encodeBytes(buffer.array());
        FamilyContentProvider.put(context, FAMILY_MEMBERS, membersString);
    }
}
