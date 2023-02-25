package org.nakedobjects.plugins.sql.objectstore;

import java.util.Date;


public interface Results {

    void close();

    int getInt(String columnName);

    long getLong(String columnName);

    String getString(String columnName);

    boolean next();

    Date getDate(String lastActivityDateColumn);

    Object getObject(String column);
}
// Copyright (c) Naked Objects Group Ltd.
