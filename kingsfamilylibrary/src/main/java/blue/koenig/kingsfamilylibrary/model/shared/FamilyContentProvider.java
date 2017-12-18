package blue.koenig.kingsfamilylibrary.model.shared;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by Thomas on 13.12.2017.
 */

public class FamilyContentProvider extends ContentProvider {
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private SharedDatabase database;

    public static void put(Context context, String key, String value) {
        ContentValues values = new ContentValues();
        values.put(KeyValueTable.VALUE, value);
        context.getContentResolver().insert(Uri.parse(FamilyContract.CONTENT_URI + "/" + key), values);
    }

    public static String get(Context context, String key) {
        Cursor cursor = context.getContentResolver().query(Uri.parse(FamilyContract.CONTENT_URI + "/" + key), null, null, null, null);
        if (cursor == null) {
            return "";
        }
        String result = "";
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(KeyValueTable.VALUE));
        }
        cursor.close();
        return result;

    }

    private void initializeUriMatching() {
        sUriMatcher.addURI(FamilyContract.AUTHORITY, FamilyContract.CONTENT_PATH + "/*", 1);
    }

    @Override
    public boolean onCreate() {
        initializeUriMatching();
        try {
            database = new SharedDatabase(getContext());
            return true;
        } catch (SQLException e) {
            logger.error("Creating database error: " + e.getMessage());
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                String key = uri.getPathSegments().get(1);
                return database.getValueCursor(key);
            default:
                // Alternatively, throw an exception.
                logger.error("Unknown uri");
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return FamilyContract.SINGLE_RECORD_MIME_TYPE;
            default:
                // Alternatively, throw an exception.
                logger.error("Unknown uri");
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                String key = uri.getPathSegments().get(1);
                String value = contentValues.getAsString(KeyValueTable.VALUE);
                try {
                    database.putValue(key, value);
                    return Uri.parse(FamilyContract.CONTENT_PATH + "/" + key);
                } catch (SQLException e) {
                    logger.error("Error putting value: " + key + ", " + value);
                }
            default:
                // Alternatively, throw an exception.
                logger.error("Unknown uri");
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        logger.error("Unsupported operation");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        logger.error("Unsupported operation");
        return 0;
    }
}
