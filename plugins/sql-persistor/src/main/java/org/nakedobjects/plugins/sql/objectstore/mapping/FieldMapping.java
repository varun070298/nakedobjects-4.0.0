package org.nakedobjects.plugins.sql.objectstore.mapping;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.sql.objectstore.Results;


public interface FieldMapping {
    void appendColumnDefinitions(StringBuffer sql);
    
    void appendColumnNames(StringBuffer sql);

    void appendInsertValues(StringBuffer sql, NakedObject object);

    void appendUpdateValues(StringBuffer sql, NakedObject object);
    
    void initializeField(NakedObject object, Results rs);


}
// Copyright (c) Naked Objects Group Ltd.
