package blue.koenig.kingsfamilylibrary.model.shared;

import android.net.Uri;

/**
 * Created by Thomas on 13.12.2017.
 */

public final class FamilyContract {

    public static final String AUTHORITY = "blue.koenig.kingsfamily.provider";
    public static final String CONTENT_PATH = "words";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    public static final int ALL_ITEMS = -2;
    public static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTENT_PATH;

    private FamilyContract() {
    }


}
