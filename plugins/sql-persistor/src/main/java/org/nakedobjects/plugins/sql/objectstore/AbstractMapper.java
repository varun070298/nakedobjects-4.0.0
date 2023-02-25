package org.nakedobjects.plugins.sql.objectstore;

import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.sql.objectstore.SqlOid.State;


public abstract class AbstractMapper {
    private FieldMappingLookup objectMapperLookup;
    private Hashtable keyMapping = new Hashtable();

    public abstract void createTables(final DatabaseConnector connector);

    protected boolean needsTables(final DatabaseConnector connector) {
        return false;
    }

    public final void shutdown() {}

    public void startup(final DatabaseConnector connector, final FieldMappingLookup objectMapperLookup) {
        this.objectMapperLookup = objectMapperLookup;
        if (needsTables(connector)) {
            createTables(connector);
        }
    }

    protected FieldMappingLookup getFieldMappingLookup() {
        return objectMapperLookup;
    }

    // TODO remove
    protected Oid recreateOid(final Results rs, final NakedObjectSpecification cls, final String column) {
        PrimaryKey key;
        if (keyMapping.containsKey(column)) {
            key = ((PrimaryKeyMapper) keyMapping.get(column)).generateKey(rs, column);
        } else {
            Object object = rs.getObject(column);
            if (object == null) {
                return null;
            } else {
                int id = ((Integer) object).intValue();
                key = new IntegerPrimaryKey(id);
            }
        }
        Oid oid = new SqlOid(cls.getFullName(), key, State.PERSISTENT);
        return oid;
    }

    // TODO remove
    protected void addPrimaryKeyMapper(final String columnName, final PrimaryKeyMapper mapper) {
        keyMapping.put(columnName, mapper);
    }

    protected String asSqlName(final String name) {
        return name.toUpperCase();
    }

    // TODO remove
    protected SerialNumberVersion createVersion(final long versionSequence) {
        return new SerialNumberVersion(versionSequence, "", new Date());
    }
}
// Copyright (c) Naked Objects Group Ltd.
