package blue.koenig.kingsfamilylibrary.model.shared;

/**
 * Created by Thomas on 06.09.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SharedDatabase extends SQLiteOpenHelper {
    // Database Name
    public static final String DATABASE_NAME = "shared_data.sqlite";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private KeyValueTable keyValueTable;
    private Context context;

    public SharedDatabase(Context context, String databaseName) throws SQLException {
        this(context, databaseName, null, DATABASE_VERSION);
    }

    public SharedDatabase(Context context) throws SQLException {
        this(context, DATABASE_NAME);
    }

    public SharedDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) throws SQLException {
        super(context, name, factory, version);
        this.context = context;
        if (keyValueTable == null) {
            keyValueTable = new KeyValueTable(getWritableDatabase());
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "On Create");
        try {
            if (keyValueTable == null) {
                keyValueTable = new KeyValueTable(db);
            }

            keyValueTable.create();
        } catch (SQLException e) {
            logger.error("Error creating shared key value table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO
    }

    public String getValue(String key) throws SQLException {
        return keyValueTable.getValue(key);
    }

    public Cursor getValueCursor(String key) {
        return keyValueTable.getValueCursor(key);
    }

    public void putValue(String key, String value) throws SQLException {
        keyValueTable.put(key, value);
    }
}
