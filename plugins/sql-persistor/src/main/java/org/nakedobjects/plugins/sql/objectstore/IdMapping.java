package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;


public class IdMapping extends IdMappingAbstract implements FieldMapping {

    public void init() {
        String idColumn = null;
        // idColumn = configParameters.getString(parameterBase + "id");
        if (idColumn == null) {
            idColumn = "PK_ID";
        }
        idColumn = FieldNameMapper.getInstance().getColumnName(idColumn);
        setColumn(idColumn);
    }

    public void appendUpdateValues(StringBuffer sql, NakedObject object) {}

    public void initializeField(NakedObject object, Results rs) {}
}

// Copyright (c) Naked Objects Group Ltd.
