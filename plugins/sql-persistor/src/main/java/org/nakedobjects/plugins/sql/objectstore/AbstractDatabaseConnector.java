package org.nakedobjects.plugins.sql.objectstore;

public abstract class AbstractDatabaseConnector implements DatabaseConnector {
    private boolean isUsed;

    public final void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public final boolean isUsed() {
        return isUsed;
    }

    private DatabaseConnectorPool pool;

    public final void setConnectionPool(DatabaseConnectorPool pool) {
        this.pool = pool;
    }

    public final DatabaseConnectorPool getConnectionPool() {
        return pool;
    }
}
// Copyright (c) Naked Objects Group Ltd.
