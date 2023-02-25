package org.nakedobjects.plugins.sql.objectstore.mapping;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.sql.objectstore.Results;


public interface ObjectReferenceMapping {

    void appendColumnDefinitions(StringBuffer sql);

    void appendInsertValues(StringBuffer sb, NakedObject value);

    void appendColumnNames(StringBuffer sql);

    void appendUpdateValues(StringBuffer sql, NakedObject object);

    Oid recreateOid(final Results rs, final NakedObjectSpecification specification);
        
}

// Copyright (c) Naked Objects Group Ltd.
