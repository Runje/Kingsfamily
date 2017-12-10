package com.koenig.commonModel.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected Connection connection;
    protected List<DatabaseTable> tables = new ArrayList<>();

    public Database(Connection connection) {
        this.connection = connection;
    }

    public void createAllTables() throws SQLException {
        for (DatabaseTable table : tables) {
            if (!table.isExisting()) {
                table.create();
                logger.info("Table created: " + table.getTableName());
            }
        }
    }

    public void deleteAllEntrys() throws SQLException {
        for (DatabaseTable table : tables) {
            table.deleteAllEntrys();
        }
    }


    /**
     * Runs a transaction and locks a specific table. Rollback on exception.
     *
     * @param runnable
     * @param table
     * @throws SQLException
     */
    protected void startTransaction(Transaction runnable, DatabaseTable table) throws SQLException {
        table.lock.lock();
        try {
            connection.setAutoCommit(false);
            runnable.run();
            connection.commit();
        } catch (Exception e) {
            logger.error("Error on transaction: " + e.getMessage());
            connection.rollback();
            logger.info("Rolled back");
            throw e;
        } finally {
            connection.setAutoCommit(true);
            table.lock.unlock();
        }
    }

    /**
     * Runs a transaction and locks all tables. Rollback on Exception
     *
     * @param runnable
     * @throws SQLException
     */
    protected void startTransaction(Transaction runnable) throws SQLException {
        for (DatabaseTable table : tables) {
            table.lock.lock();
        }
        try {
            connection.setAutoCommit(false);
            runnable.run();
            connection.commit();
        } catch (Exception e) {
            logger.error("Error on transaction: " + e.getMessage());
            connection.rollback();
            logger.info("Rolled back");
            throw e;
        } finally {
            connection.setAutoCommit(true);
            for (DatabaseTable table : tables) {
                table.lock.unlock();
            }
        }
    }


    public interface Transaction {
        void run() throws SQLException;
    }

    public interface ResultTransaction<X> {
        X run() throws SQLException;
    }


}
