package org.nakedobjects.plugins.sql.objectstore.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.sql.objectstore.AbstractDatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStore;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStoreException;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class JdbcConnector extends AbstractDatabaseConnector {
    private static final Logger LOG = Logger.getLogger(JdbcConnector.class);
    private Connection connection;

    public void close() {
        try {
            if (connection != null) {
                LOG.info("close");
                connection.close();
            }
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Failed to close", e);
        }
    }

    public int count(final String sql) {
        LOG.debug("SQL: " + sql);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            result.next();
            int count = result.getInt(1);
            statement.close();
            return count;
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Failed count", e);
        }
    }

    public void delete(final String sql) {
        update(sql);
    }

    public void open() {
        try {
            NakedObjectConfiguration params = NakedObjectsContext.getConfiguration();
            String BASE = SqlObjectStore.BASE_NAME + ".jdbc.";
            String driver = params.getString(BASE + "driver");
            String url = params.getString(BASE + "connection");
            String user = params.getString(BASE + "user");
            String password = params.getString(BASE + "password");

            if (driver == null) {
                throw new SqlObjectStoreException("No driver specified for database connection");
            }
            if (url == null) {
                throw new SqlObjectStoreException("No connection URL specified to database");
            }
            if (user == null) {
                throw new SqlObjectStoreException("No user specified for database connection");
            }
            if (password == null) {
                throw new SqlObjectStoreException("No password specified for database connection");
            }

            Class.forName(driver);
            LOG.info("Connecting to " + url + " as " + user);
            connection = DriverManager.getConnection(url, user, password);
            if (connection == null) {
                throw new SqlObjectStoreException("No connection established to " + url);
            }
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Failed to start", e);
        } catch (ClassNotFoundException e) {
            throw new SqlObjectStoreException("Could not find database driver", e);
        }
    }

    /*
     * public void executeStoredProcedure(final StoredProcedure storedProcedure) { Parameter[] parameters =
     * storedProcedure.getParameters(); StringBuffer sql = new StringBuffer("{call ");
     * sql.append(storedProcedure.getName()); sql.append(" ("); for (int i = 0, no = parameters.length; i <
     * no; i++) { sql.append(i == 0 ? "?" : ",?"); } sql.append(")}"); LOG.debug("SQL: " + sql);
     * 
     * CallableStatement statement; try { statement = connection.prepareCall(sql.toString());
     * 
     * for (int i = 0; i < parameters.length; i++) { LOG.debug(" setup param " + i + " " + parameters[i]);
     * parameters[i].setupParameter(i + 1, parameters[i].getName(), storedProcedure); } LOG.debug(" execute
     * "); statement.execute(); for (int i = 0; i < parameters.length; i++) { parameters[i].retrieve(i + 1,
     * parameters[i].getName(), storedProcedure); LOG.debug(" retrieve param " + i + " " + parameters[i]); } }
     * catch (SQLException e) { throw new NakedObjectRuntimeException(e); }
     * 
     *  }
     * 
     * 
     * public MultipleResults executeStoredProcedure(final String name, final Parameter[] parameters) {
     * StringBuffer sql = new StringBuffer("{call "); sql.append(name); sql.append(" ("); for (int i = 0; i <
     * parameters.length; i++) { sql.append(i == 0 ? "?" : ",?"); } sql.append(")}"); LOG.debug("SQL: " +
     * sql);
     * 
     * CallableStatement statement; try { statement = connection.prepareCall(sql.toString());
     * 
     * StoredProcedure storedProcedure = new JdbcStoredProcedure(statement);
     * 
     * for (int i = 0; i < parameters.length; i++) { LOG.debug(" setup param " + i + " " + parameters[i]);
     * parameters[i].setupParameter(i + 1, parameters[i].getName(), storedProcedure); } LOG.debug(" execute
     * "); statement.execute(); for (int i = 0; i < parameters.length; i++) { parameters[i].retrieve(i + 1,
     * parameters[i].getName(), storedProcedure); LOG.debug(" retrieve param " + i + " " + parameters[i]); }
     * 
     * return new JdbcResults(statement); } catch (SQLException e) { throw new NakedObjectRuntimeException(e); }
     *  }
     */

    public Results select(final String sql) {
        LOG.debug("SQL: " + sql);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            return new JdbcResults(statement.executeQuery());
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public int update(final String sql) {
        LOG.debug("SQL: " + sql);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            int updateCount = statement.executeUpdate();
            statement.close();
            return updateCount;
        } catch (SQLException e) {
            LOG.error("failed to execute " + sql);
            throw new SqlObjectStoreException("SQL error", e);
        }
    }

    public boolean hasTable(final String tableName) {
        try {
            ResultSet set = connection.getMetaData().getTables(null, null, tableName, null);
            if (set.next()) {
                LOG.debug("Found " + set.getString("TABLE_NAME"));
                set.close();
                return true;
            } else {
                set.close();
                return false;
            }
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public void insert(final String sql) {
        update(sql);
    }

    public void insert(final String sql, final Object oid) {
        LOG.debug("SQL: " + sql);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            /*
             * require 3.0 ResultSet rs = statement.getGeneratedKeys(); if(rs.next()) { int id = rs.getInt(1); }
             */statement.close();
        } catch (SQLException e) {
            throw new SqlObjectStoreException("SQL error", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void commit() {
        try {
            LOG.debug("commit");
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Commit error", e);
        }
    }

    public void begin() {
        try {
            LOG.debug("begin transaction");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Rollback error", e);
        }

    }

    public void rollback() {
        try {
            LOG.debug("rollback");
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SqlObjectStoreException("Rollback error", e);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
