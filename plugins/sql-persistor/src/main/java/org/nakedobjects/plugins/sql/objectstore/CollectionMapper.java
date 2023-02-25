package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface CollectionMapper {

    public void loadInternalCollection(final DatabaseConnector connector, final NakedObject parent);

    public void saveInternalCollection(final DatabaseConnector connector, final NakedObject parent);

    void createTables(DatabaseConnector connection);

    boolean needsTables(DatabaseConnector connection);
}
// Copyright (c) Naked Objects Group Ltd.
