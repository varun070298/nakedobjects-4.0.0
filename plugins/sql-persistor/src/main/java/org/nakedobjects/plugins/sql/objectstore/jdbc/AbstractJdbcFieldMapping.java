package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.sql.objectstore.FieldNameMapper;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;


public abstract class AbstractJdbcFieldMapping implements FieldMapping {
    private String columnName;
    private final NakedObjectAssociation field;
    
    
    public AbstractJdbcFieldMapping(NakedObjectAssociation field) {
        this.field = field;
        columnName = FieldNameMapper.getInstance().getColumnName(field.getName());
    }

    public void appendColumnDefinitions(StringBuffer sql) {
        sql.append(columnName);
        sql.append(" ");
        sql.append(columnType());
    }

    public void appendColumnNames(StringBuffer sql) {
        sql.append(columnName);
    }

    public void appendInsertValues(StringBuffer sql, NakedObject object) {
        NakedObject fieldValue = field.get(object);
        if (fieldValue == null) {
            sql.append("NULL");
        } else {
            sql.append(valueAsDBString(fieldValue));
        }
    }

    public void appendUpdateValues(StringBuffer sql, NakedObject object) {
        sql.append(FieldNameMapper.getInstance().getColumnName(field.getName()));
        sql.append('=');
        NakedObject fieldValue = field.get(object);
        sql.append(valueAsDBString(fieldValue));
    }

    public void initializeField(NakedObject object, Results rs) {
        String columnName = FieldNameMapper.getInstance().getColumnName(field.getName());
        String encodedValue = (String) rs.getString(columnName);
        NakedObject restoredValue;
        if (encodedValue == null) {
            restoredValue = null;
        } else {
            restoredValue = setFromDBColumn(encodedValue, field);
            
        }
        ((OneToOneAssociation) field).initAssociation(object, restoredValue);
    }

    protected abstract String columnType();

    protected abstract String valueAsDBString(NakedObject value);

    protected abstract NakedObject setFromDBColumn(String encodeValue, NakedObjectAssociation field);

}

// Copyright (c) Naked Objects Group Ltd.
