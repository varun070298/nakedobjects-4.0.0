package org.nakedobjects.plugins.sql.objectstore.auto;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.DatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.IdMapping;
import org.nakedobjects.plugins.sql.objectstore.ObjectMapping;
import org.nakedobjects.plugins.sql.objectstore.ObjectMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStoreException;
import org.nakedobjects.plugins.sql.objectstore.VersionMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.persistence.PersistorUtil;


public class AutoMapper extends AbstractAutoMapper implements ObjectMapping {
    private static final Logger LOG = Logger.getLogger(AutoMapper.class);
    private static final int MAX_INSTANCES = 100;
    private String instancesWhereClause;
    private final IdMapping idMapping;
    private final VersionMapping versionMapping;
    
    public AutoMapper(final String nakedClassName, final String parameterBase, FieldMappingLookup lookup) {
        super(nakedClassName, parameterBase, lookup);
        idMapping = lookup.createIdMapping();
        versionMapping = lookup.createVersionMapping();
    }

    public void createObject(final DatabaseConnector connector, final NakedObject object) {
        int versionSequence = 1;
        SerialNumberVersion version = createVersion(versionSequence);

        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + table + " (");
        idMapping.appendColumnNames(sql);
        sql.append(", ");
        sql.append(columnList());
        sql.append(", ");
        sql.append(versionMapping.insertColumns());
        sql.append(") values (" );
        idMapping.appendInsertValues(sql, object);
        sql.append(", ");
        sql.append(values(object));
        sql.append(versionMapping.insertValues(version));
        sql.append(") " );
        
        connector.insert(sql.toString());
        object.setOptimisticLock(version);

        for (int i = 0; i < collectionMappers.length; i++) {
            collectionMappers[i].saveInternalCollection(connector, object);
        }
    }

    public void createTables(final DatabaseConnector connection) {
        if (!connection.hasTable(table)) {
            StringBuffer sql = new StringBuffer();
            sql.append("create table ");
            sql.append(table);
            sql.append(" (");
            idMapping.appendColumnDefinitions(sql);
            sql.append(", ");
            for (FieldMapping mapping : fieldMappings) {
                mapping.appendColumnDefinitions(sql);
                sql.append(",");
            }
            sql.append(versionMapping.appendColumnDefinitions());
            sql.append(")");
            connection.update(sql.toString());
        }
        for (int i = 0; collectionMappers != null && i < collectionMappers.length; i++) {
            if (collectionMappers[i].needsTables(connection)) {
                collectionMappers[i].createTables(connection);
            }
        }
    }

    public void destroyObject(final DatabaseConnector connector, final NakedObject object) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + table + " where ");
        idMapping.appendWhereClause(sql, object);
        sql.append(versionMapping.whereClause((SerialNumberVersion) object.getVersion()));
        int updateCount = connector.update(sql.toString());
        if (updateCount == 0) {
            LOG.info("concurrency conflict object " + this + "; no deletion performed");
            throw new ConcurrencyException("", object.getOid());
        }
 
        /*
        String id = primaryKey(object.getOid());
        String statement = "delete from " + table + " where " + idColumn + " = " + id + " and " + versionColumn + " = "
                + ((SerialNumberVersion) object.getVersion()).getSequence();
        connector.update(statement);
        */
    }

    public NakedObject[] getInstances(final DatabaseConnector connector, final NakedObjectSpecification spec) {
        return loadInstances(connector, spec, createSelectStatement(null, null));
    }

    private String createSelectStatement(final String whereClause, final Oid oid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        idMapping.appendColumnNames(sql);
        sql.append(", ");
        sql.append(columnList());
        sql.append(", ");
        sql.append(versionMapping.insertColumns());
        sql.append(" from " + table);
        if (whereClause != null) {
            sql.append(" where ");
            sql.append(whereClause);
        } else if (whereClause != null) {
            sql.append(" where ");
            idMapping.appendWhereClause(sql, oid);            
        }
        sql.append(" order by ");
        idMapping.appendColumnNames(sql);
        return sql.toString();
    }

    public NakedObject[] getInstances(final DatabaseConnector connector, final NakedObjectSpecification spec, final String pattern) {
        String where = instancesWhereClause + pattern;
        return loadInstances(connector, spec, createSelectStatement(where, null));
    }

    public NakedObject getObject(final DatabaseConnector connector, final Oid oid, final NakedObjectSpecification hint) {
        Results rs = connector.select( createSelectStatement(null, oid));
        rs.next();
        return loadObject(connector, hint, rs);
    }

    public boolean hasInstances(final DatabaseConnector connector, final NakedObjectSpecification cls) {
        String statement = "select count(*) from " + table;
        int instances = connector.count(statement);
        return instances > 0;
    }

    protected void loadFields(final NakedObject object, final Results rs) {
        PersistorUtil.start(object, ResolveState.RESOLVING);
        for (FieldMapping mapping  : fieldMappings) {
            mapping.initializeField(object, rs);
        }
/*
        for (int i = 0; i < oneToManyProperties.length; i++) {
            /*
             * Need to set up collection to be a ghost before we access as below
             */
            // NakedCollection collection = (NakedCollection)
   /*         oneToManyProperties[i].get(object);
        }
*/
        

        object.setOptimisticLock(versionMapping.getLock(rs));
        PersistorUtil.end(object);

    }

    private NakedObject[] loadInstances(
            final DatabaseConnector connector,
            final NakedObjectSpecification cls,
            final String selectStatment) {
        LOG.debug("loading instances from SQL " + table);
        Vector instances = new Vector();

        Results rs = connector.select(selectStatment);
        for (int count = 0; rs.next() && count < MAX_INSTANCES; count++) {
            NakedObject instance = loadObject(connector, cls, rs);
            LOG.debug("  instance  " + instance);
            instances.addElement(instance);
        }
        rs.close();

        NakedObject[] array = new NakedObject[instances.size()];
        instances.copyInto(array);
        return array;
    }

    private NakedObject loadObject(final DatabaseConnector connector, final NakedObjectSpecification cls, final Results rs) {
        Oid oid = idMapping.recreateOid(rs,  specification);
        NakedObject instance = getAdapter(cls, oid);

        if (instance.getResolveState().isValidToChangeTo(ResolveState.RESOLVING)) {
            loadFields(instance, rs);
            // loadCollections(connector, instance);
        }
        return instance;
    }

    public void resolve(final DatabaseConnector connector, final NakedObject object) {
        LOG.debug("loading data from SQL " + table + " for " + object);
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append(columnList());
        sql.append(",");
        sql.append(versionMapping.appendSelectColumns());
        sql.append(" from " + table + " where ");
        idMapping.appendWhereClause(sql, object);

        Results rs = connector.select(sql.toString());
        if (rs.next()) {
            loadFields(object, rs);
            rs.close();

            for (int i = 0; i < collectionMappers.length; i++) {
                collectionMappers[i].loadInternalCollection(connector, object);
            }
        } else {
            rs.close();
            throw new SqlObjectStoreException("Unable to load data from " + table + " with id " + idMapping.primaryKey(object.getOid()));
        }
    }

    public void resolveCollection(final DatabaseConnector connector, final NakedObject object, final NakedObjectAssociation field) {
        if (collectionMappers.length > 0) {
            DatabaseConnector secondConnector = connector.getConnectionPool().acquire();
            for (int i = 0; i < collectionMappers.length; i++) {
                collectionMappers[i].loadInternalCollection(secondConnector, object);
            }
            connector.getConnectionPool().release(secondConnector);
        }
    }

    public void startup(final DatabaseConnector connector, final ObjectMappingLookup objectMapperLookup) {
        if (needsTables(connector)) {
            createTables(connector);
        }
    }

    public void save(final DatabaseConnector connector, final NakedObject object) {
        SerialNumberVersion version =  (SerialNumberVersion) object.getVersion();
        long nextSequence = version.getSequence() + 1;
        
        StringBuffer sql = new StringBuffer();
        sql.append( "update " + table + " set ");
        for (FieldMapping mapping  : fieldMappings) {
            mapping.appendUpdateValues(sql, object);
            sql.append(", ");
        }
        sql.append(versionMapping.updateAssigment(nextSequence));
        sql.append( " where ");
        idMapping.appendWhereClause(sql, object);
        sql.append( " and ");
        sql.append(versionMapping.whereClause((SerialNumberVersion) object.getVersion()));
       
        int updateCount = connector.update(sql.toString());
        if (updateCount == 0) {
            LOG.info("concurrency conflict object " + this + "; no update performed");
            throw new ConcurrencyException("", object.getOid());
        } else {
            object.setOptimisticLock(createVersion(nextSequence));
        }

        // TODO update collections - change only when needed rather than reinserting from scratch
        for (int i = 0; i < collectionMappers.length; i++) {
            collectionMappers[i].saveInternalCollection(connector, object);
        }
    }

    public String toString() {
        return "AutoMapper [table=" + table + ",id=" + idMapping + ",noColumns=" + fieldMappings.size() + ",nakedClass="
                + specification.getFullName() + "]";
    }

}
// Copyright (c) Naked Objects Group Ltd.
