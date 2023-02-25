package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMappingFactory;

public class JdbcObjectReferenceMappingFactory implements ObjectReferenceMappingFactory {

    public ObjectReferenceMapping createReferenceMapping(NakedObjectSpecification specification) {
        return new JdbcObjectReferenceMapping(specification.getShortName(), specification);
    }

}


// Copyright (c) Naked Objects Group Ltd.
