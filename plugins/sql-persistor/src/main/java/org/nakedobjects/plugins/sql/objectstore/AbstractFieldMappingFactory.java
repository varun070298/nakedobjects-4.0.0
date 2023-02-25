package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;



public abstract class AbstractFieldMappingFactory implements FieldMappingFactory {
    final String type;

    public AbstractFieldMappingFactory(final String type) {
        this.type = type;
    }
}

// Copyright (c) Naked Objects Group Ltd.
