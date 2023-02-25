package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.sql.objectstore.FieldNameMapper;
import org.nakedobjects.plugins.sql.objectstore.IdMappingAbstract;
import org.nakedobjects.plugins.sql.objectstore.IntegerPrimaryKey;
import org.nakedobjects.plugins.sql.objectstore.PrimaryKey;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStoreException;
import org.nakedobjects.plugins.sql.objectstore.SqlOid;
import org.nakedobjects.plugins.sql.objectstore.SqlOid.State;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMapping;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public class JdbcObjectReferenceMapping extends IdMappingAbstract implements ObjectReferenceMapping {
    private NakedObjectSpecification specification;

    public JdbcObjectReferenceMapping(String columnName, NakedObjectSpecification specification) {
        this.specification = specification;
        String idColumn = FieldNameMapper.getInstance().getColumnName("FK_" + columnName);
        setColumn(idColumn);
    }

     public void appendUpdateValues(StringBuffer sql, NakedObject object) {
        sql.append(getColumn());
        if (object == null) {
            sql.append("= NULL ");
        } else {
            sql.append("='");
            sql.append(primaryKey(object.getOid()));
            sql.append("'");
        }
    }

    public NakedObject initializeField(Results rs) {
        Oid oid = recreateOid(rs, specification);
        if (oid != null) {
            if (specification.isAbstract()) {
                throw new SqlObjectStoreException("NOT DEALING WITH POLYMORPHIC ASSOCIATIONS");
            } else {
                return getAdapter(specification, oid);
            }
        } else {
            return null;
        }
    }


}

// Copyright (c) Naked Objects Group Ltd.
