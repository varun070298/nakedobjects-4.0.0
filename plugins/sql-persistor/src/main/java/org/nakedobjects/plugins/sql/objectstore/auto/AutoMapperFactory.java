package org.nakedobjects.plugins.sql.objectstore.auto;

import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.ObjectMapping;
import org.nakedobjects.plugins.sql.objectstore.ObjectMappingFactory;


public class AutoMapperFactory implements ObjectMappingFactory {
    public ObjectMapping createMapper(final String className, final String propertiesBase, FieldMappingLookup lookup) {
        return new AutoMapper(className, propertiesBase, lookup);
    }
}
// Copyright (c) Naked Objects Group Ltd.
