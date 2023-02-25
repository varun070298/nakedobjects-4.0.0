package org.nakedobjects.plugins.sql.objectstore;

public interface DatabaseConnector {
    /*
     * @deprecated Results callStoredProcedure(String name, Parameter[] parameters);
     */
    void close();

    int count(String sql);

    void delete(String sql);

    // MultipleResults executeStoredProcedure(String name, Parameter[] parameters);

    boolean hasTable(String tableName);

    void insert(String sql);

    void insert(String sql, Object oid);

    Results select(String sql);

    /**
     * Updates the database using the specified sql statement, and returns the number of rows affected.
     */
    int update(String sql);

    void setUsed(boolean isUsed);

    boolean isUsed();

    void commit();

    void rollback();

    void setConnectionPool(DatabaseConnectorPool pool);

    DatabaseConnectorPool getConnectionPool();

    void begin();
}
// Copyright (c) Naked Objects Group Ltd.
