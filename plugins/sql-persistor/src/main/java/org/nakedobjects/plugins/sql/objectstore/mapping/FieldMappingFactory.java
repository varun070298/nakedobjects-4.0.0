package org.nakedobjects.plugins.sql.objectstore.mapping;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public interface FieldMappingFactory {
    FieldMapping createFieldMapping(final NakedObjectAssociation field);
}

// Copyright (c) Naked Objects Group Ltd.
