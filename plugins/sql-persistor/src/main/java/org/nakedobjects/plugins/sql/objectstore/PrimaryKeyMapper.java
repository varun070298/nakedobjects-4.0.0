package org.nakedobjects.plugins.sql.objectstore;

public interface PrimaryKeyMapper {
    PrimaryKey generateKey(Results rs, String column);
}
// Copyright (c) Naked Objects Group Ltd.
