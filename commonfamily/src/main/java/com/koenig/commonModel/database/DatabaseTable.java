package com.koenig.commonModel.database;

import com.koenig.commonModel.Item;
import com.koenig.commonModel.User;
import com.koenig.communication.messages.FamilyMessage;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Thomas on 25.11.2017.
 */

public abstract class DatabaseTable<T extends Item> {
    public static final String COLUMN_INSERT_DATE = "insert_date";
    public static final String COLUMN_MODIFIED_DATE = "modified_date";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INSERT_ID = "insert_id";
    public static final String COLUMN_MODIFIED_ID = "modified_id";
    public static final String COLUMN_DELETED = "deleted";
    public static final int FALSE = 0;
    public static final int TRUE = 1;
    public static final String FALSE_STRING = "0";
    public static final String TRUE_STRING = "1";
    public static final String STRING_LIST_SEPARATOR = ";";
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected ReentrantLock lock = new ReentrantLock();

    public static String buildStringList(List<String> list) {
        if (list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String s : list) {
                builder.append(s + STRING_LIST_SEPARATOR);
            }

            return builder.substring(0, builder.length() - 1);
        } else {
            return "";
        }
    }

    public static List<String> getStringList(String listAsString) {
        if (listAsString.isEmpty()) {
            return new ArrayList<>();
        }

        String[] items = listAsString.split(STRING_LIST_SEPARATOR);
        // need to make a copy to allow List.add method! (Alternative create list  manually)
        return new ArrayList<>(Arrays.asList(items));
    }

    public abstract String getTableName();

    public abstract void create() throws SQLException;

    public abstract List<DatabaseItem<T>> getAll() throws SQLException;

    public abstract boolean isExisting() throws SQLException;

    public abstract void add(DatabaseItem<T> databaseItem) throws SQLException;

    public abstract DatabaseItem<T> getDatabaseItemFromId(String id) throws SQLException;

    public abstract T getFromId(String id) throws SQLException;

    public abstract void deleteAllEntrys() throws SQLException;

    public abstract void deleteFrom(String itemId, String userId) throws SQLException;

    public abstract void updateFrom(T item, String userId) throws SQLException;

    public ReentrantLock getLock() {
        return lock;
    }

    public List<T> getAllItems() throws SQLException {
        return toItemList(getAll());
    }

    public void addFrom(T item, String userId) throws SQLException {
        runInLock(() -> {
            DateTime now = DateTime.now();
            DatabaseItem<T> databaseItem = new DatabaseItem<>(item, now, now, false, userId, userId);
            add(databaseItem);
        });
    }

    protected abstract List<String> getColumnNames();

    /**
     * Shall return the create statement for the specific tables field, i.e for field name and birthday:
     * name TEXT, birthday LONG
     *
     * @return
     */
    protected abstract String getTableSpecificCreateStatement();

    public List<T> toItemList(List<DatabaseItem<T>> list) {
        List<T> users = new ArrayList<>(list.size());
        for (DatabaseItem<T> user : list) {
            users.add(user.getItem());
        }

        return users;
    }

    protected void runInLock(Database.Transaction runnable) throws SQLException {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    protected <X> X runInLock(Database.ResultTransaction<X> runnable) throws SQLException {
        lock.lock();
        try {
            return runnable.run();
        } finally {
            lock.unlock();
        }
    }

    protected String buildCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS " + getTableName() + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_DELETED + " INT, " +
                COLUMN_INSERT_DATE + " LONG, " +
                COLUMN_INSERT_ID + " TEXT, " +
                COLUMN_MODIFIED_DATE + " LONG, " +
                COLUMN_MODIFIED_ID + " TEXT, " +
                COLUMN_NAME + " TEXT" +
                getTableSpecificCreateStatement() +
                ");";
    }

    protected List<String> getBaseColumnNames() {
        List<String> columns = new ArrayList<>();
        columns.add(COLUMN_ID);
        columns.add(COLUMN_DELETED);
        columns.add(COLUMN_INSERT_DATE);
        columns.add(COLUMN_INSERT_ID);
        columns.add(COLUMN_MODIFIED_DATE);
        columns.add(COLUMN_MODIFIED_ID);
        columns.add(COLUMN_NAME);
        return columns;
    }

    protected List<User> getUsers(UserService userService, String usersText) {
        List<User> users = new ArrayList<>();
        if (!usersText.isEmpty()) {
            String[] userIds = usersText.split(FamilyMessage.SEPARATOR);
            for (String id :
                    userIds) {
                try {
                    users.add(userService.getUserFromId(id));
                } catch (SQLException e) {
                    // don't add user to result
                    logger.error("Couldn't find user with id: " + id);
                }
            }
        }

        return users;
    }

    protected String usersToId(List<User> users) {
        StringBuilder builder = new StringBuilder();
        for (User user : users) {
            builder.append(user.getId());
            builder.append(FamilyMessage.SEPARATOR);
        }

        String result = users.size() > 0 ? builder.substring(0, builder.length() - 1) : "";
        return result;
    }

}
