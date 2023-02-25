package org.nakedobjects.plugins.sql.objectstore.mapping;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public interface ObjectReferenceMappingFactory {
    ObjectReferenceMapping createReferenceMapping(NakedObjectSpecification specification);
}

// Copyright (c) Naked Objects Group Ltd.
