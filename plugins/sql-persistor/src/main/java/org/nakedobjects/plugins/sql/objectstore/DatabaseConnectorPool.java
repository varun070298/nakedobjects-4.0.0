package org.nakedobjects.plugins.sql.objectstore;

import java.util.Vector;

import org.apache.log4j.Logger;


public class DatabaseConnectorPool {
    private static final Logger LOG = Logger.getLogger(DatabaseConnectorPool.class);
    private static final int AVERAGE_POOL_SIZE = 5;

    private final DatabaseConnectorFactory factory;
    private final Vector connectorPool;

    public DatabaseConnectorPool(final DatabaseConnectorFactory factory) {
        this(factory, AVERAGE_POOL_SIZE);
    }

    public DatabaseConnectorPool(final DatabaseConnectorFactory factory, final int size) {
        this.factory = factory;
        connectorPool = new Vector();
        for (int i = 0; i < size; i++) {
            newConnector();
        }
        LOG.info("Created an intial pool of " + size + " database connections");
    }

    private DatabaseConnector newConnector() {
        DatabaseConnector connector = factory.createConnector();
        connector.setConnectionPool(this);
        connectorPool.addElement(connector);
        return connector;
    }

    public DatabaseConnector acquire() {
        DatabaseConnector connector = findFreeConnector();
        if (connector == null) {
            connector = newConnector();
            connector.setUsed(true);
            LOG.info("Added an additional database connection; now contains " + connectorPool.size() + " connections");
        }
        LOG.debug("acquired connection " + connector);
        return connector;
    }

    private DatabaseConnector findFreeConnector() {
        for (int i = 0, no = connectorPool.size(); i < no; i++) {
            DatabaseConnector connector = (DatabaseConnector) connectorPool.elementAt(i);
            if (!connector.isUsed()) {
                connector.setUsed(true);
                return connector;
            }
        }
        return null;
    }

    public void release(final DatabaseConnector connector) {
        connector.setUsed(false);
        LOG.debug("released connection " + connector);
    }

    public void shutdown() {
        for (int i = 0, no = connectorPool.size(); i < no; i++) {
            DatabaseConnector connector = (DatabaseConnector) connectorPool.elementAt(i);
            try {
                connector.close();
            } catch (SqlObjectStoreException e) {
                LOG.error("Failed to release connectuion", e);
            }
        }
        connectorPool.removeAllElements();
    }
}
// Copyright (c) Naked Objects Group Ltd.
