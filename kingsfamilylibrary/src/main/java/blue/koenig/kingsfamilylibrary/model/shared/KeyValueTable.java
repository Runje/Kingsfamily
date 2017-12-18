package blue.koenig.kingsfamilylibrary.model.shared;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by Thomas on 25.11.2017.
 */

public class KeyValueTable {
    public static final String NAME = "key_value_table";
    public static final String VALUE = "value";
    private static final String KEY = "key";
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    SQLiteDatabase db;

    public KeyValueTable(SQLiteDatabase database) {
        db = database;
    }

    public void create() throws SQLException {
        db.execSQL(buildCreateStatement());
    }

    private String buildCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS " + this.getTableName() + " (" + "key" + " TEXT PRIMARY KEY, " + "value" + " TEXT );";
    }

    protected ContentValues itemToValues(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(KEY, key);
        values.put(VALUE, value);
        return values;

    }

    public void put(String key, String value) throws SQLException {
        if (key.isEmpty() || value.isEmpty()) {
            logger.error("Key or value is empty: " + key + ", " + value);
            return;
        }

        String exists = getValue(key);
        if (exists.isEmpty()) {
            db.insert(getTableName(), null, itemToValues(key, value));
        } else {
            String query = "UPDATE " + getTableName() + " SET " + VALUE + "=?" + " WHERE " + KEY + "= ?";
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindString(1, value);
            statement.bindString(2, key);
            int updates = statement.executeUpdateDelete();

            if (updates != 1) {
                throw new SQLException("Update error: rows= " + updates);
            }
        }
    }

    public String getValue(String key) throws SQLException {
        String result = "";
        Cursor cursor = getValueCursor(key);
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(KEY));
        }

        cursor.close();
        return result;
    }


    public String getTableName() {
        return NAME;
    }

    public Cursor getValueCursor(String key) {
        String selectQuery = "SELECT * FROM " + getTableName() + " WHERE " + KEY + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{key});

        return cursor;
    }
}