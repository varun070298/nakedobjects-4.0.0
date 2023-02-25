package org.nakedobjects.plugins.sql.objectstore;

public class StringPrimaryKeyMapper implements PrimaryKeyMapper {

    public PrimaryKey generateKey(final Results rs, final String column) {
        String id = rs.getString(column);
        return new StringPrimaryKey(id);
    }

}
// Copyright (c) Naked Objects Group Ltd.
