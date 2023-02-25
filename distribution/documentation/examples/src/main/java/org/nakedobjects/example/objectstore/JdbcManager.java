package org.nakedobjects.example.objectstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class JdbcManager {

    private Connection connection;

    public JdbcManager() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:testdb/data", "sa", "");
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        checkConnection();
    }

    private void checkConnection() {
        if (connection == null) {
            throw new DatabaseException("Connection to database not available");
        }
    }

    public void execute(String expression) {
        checkConnection();
        try {
            Statement st = null;
            st = connection.createStatement(); 
            int i = st.executeUpdate(expression);
            if (i == -1) {
                throw new DatabaseException("db error : " + expression);
            }
            st.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void close() {
        checkConnection();
        try {
            Statement st = connection.createStatement();
            st.execute("SHUTDOWN");
            connection.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
