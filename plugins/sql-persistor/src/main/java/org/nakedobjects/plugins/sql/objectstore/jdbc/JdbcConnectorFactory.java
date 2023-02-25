package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.plugins.sql.objectstore.DatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.DatabaseConnectorFactory;


public class JdbcConnectorFactory implements DatabaseConnectorFactory {

    public DatabaseConnector createConnector() {
        JdbcConnector connection = new JdbcConnector();
        connection.open();
        return connection;
    }

}
// Copyright (c) Naked Objects Group Ltd.
