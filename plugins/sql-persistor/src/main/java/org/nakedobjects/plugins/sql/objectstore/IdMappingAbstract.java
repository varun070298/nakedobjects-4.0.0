package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.sql.objectstore.SqlOid.State;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public class IdMappingAbstract {
    private String column;

    protected void setColumn(String column) {
        this.column = column;
    }

    protected String getColumn() {
        return column;
    }

    public void appendWhereClause(StringBuffer sql, NakedObject object) {
        appendWhereClause(sql, object.getOid());
    }

    public void appendWhereClause(StringBuffer sql, Oid oid) {
        sql.append(column);
        sql.append(" = ");
        String id = primaryKey(oid);
        sql.append(id);
    }

    public void appendColumnDefinitions(StringBuffer sql) {
        sql.append(column);
        sql.append(" ");
        sql.append("INT");
    }

    public void appendColumnNames(StringBuffer sql) {
        sql.append(column);
    }

    public void appendInsertValues(StringBuffer sql, NakedObject object) {
        if (object == null) {
            sql.append("NULL");
        } else {
            sql.append(primaryKey(object.getOid()));
        }
    }

    public String primaryKey(final Oid oid) {
        return oid instanceof SqlOid ? ((SqlOid) oid).getPrimaryKey().stringValue() : "" + ((SerialOid) oid).getSerialNo();
    }

    public Oid recreateOid(final Results rs, final NakedObjectSpecification specification) {
        PrimaryKey key;
        Object object = rs.getObject(column);
        if (object == null) {
            return null;
        } else {
            int id = ((Integer) object).intValue();
            key = new IntegerPrimaryKey(id);
        }
        Oid oid = new SqlOid(specification.getFullName(), key, State.PERSISTENT);
        return oid;
    }

    protected NakedObject getAdapter(final NakedObjectSpecification specification, final Oid oid) {
        AdapterManager objectLoader = NakedObjectsContext.getPersistenceSession().getAdapterManager();
        NakedObject adapter = objectLoader.getAdapterFor(oid);
        if (adapter != null) {
            return adapter;
        } else {
            return NakedObjectsContext.getPersistenceSession().recreateAdapter(oid, specification);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
