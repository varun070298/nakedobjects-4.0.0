package org.nakedobjects.plugins.sql.objectstore;

public interface ObjectMappingFactory {
    ObjectMapping createMapper(String className, String propertiesBase, FieldMappingLookup objectMapperLookup);
}
// Copyright (c) Naked Objects Group Ltd.
