package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.sql.objectstore.FieldNameMapper;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;


public class JdbcObjectReferenceFieldMapping extends JdbcObjectReferenceMapping implements FieldMapping {
    
    public static class Factory implements FieldMappingFactory {
        public FieldMapping createFieldMapping(final NakedObjectAssociation field) {
            return new JdbcObjectReferenceFieldMapping(field);
        }
    }
    
    final NakedObjectAssociation field;
    
    public JdbcObjectReferenceFieldMapping(NakedObjectAssociation field) {
        super(columnName(field), field.getSpecification());
        this.field = field;
    }
    
    private static String columnName(NakedObjectAssociation field) {
        return FieldNameMapper.getInstance().getColumnName(field.getName());
    }

    public void appendInsertValues(StringBuffer sb,NakedObject object) {
        NakedObject fieldValue = field.get(object);
        super.appendInsertValues(sb, fieldValue);
    }
    
    public void appendUpdateValues(StringBuffer sql, NakedObject object) {
        NakedObject fieldValue = field.get(object);
        super.appendUpdateValues(sql, fieldValue);
    }
    
    public void initializeField(NakedObject object, Results rs) {
        NakedObject reference = initializeField(rs);
        ((OneToOneAssociation) field).initAssociation(object, reference);
    }
}

// Copyright (c) Naked Objects Group Ltd.
