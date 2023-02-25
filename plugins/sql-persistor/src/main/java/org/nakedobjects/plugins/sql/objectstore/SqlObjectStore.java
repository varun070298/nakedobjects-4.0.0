package org.nakedobjects.plugins.sql.objectstore;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByPattern;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.PersistenceCommand;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


public final class SqlObjectStore implements ObjectStore {
    public static final String BASE_NAME = "nakedobjects.persistence.sql";
    private static final Logger LOG = Logger.getLogger(SqlObjectStore.class);
    private DatabaseConnectorPool connectionPool;
    private ObjectMappingLookup objectMappingLookup;
    private boolean isInitialized;

    public void abortTransaction() {}

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new CreateObjectCommand() {
            public void execute(final NakedObjectTransaction context) {
                DatabaseConnector connection = ((SqlExecutionContext) context).getConnection();
                LOG.debug("  create object " + object);
                ObjectMapping mapping = objectMappingLookup.getMapping(object);
                mapping.createObject(connection, object);
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "CreateObjectCommand [object=" + object + "]";
            }
        };
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        return new DestroyObjectCommand() {
            public void execute(final NakedObjectTransaction context) {
                DatabaseConnector connection = ((SqlExecutionContext) context).getConnection();
                LOG.debug("  destroy object " + object);
                ObjectMapping mapping = objectMappingLookup.getMapping(object);
                mapping.destroyObject(connection, object);
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "DestroyObjectCommand [object=" + object + "]";
            }
        };
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        return new SaveObjectCommand() {
            public void execute(final NakedObjectTransaction context) {
                DatabaseConnector connection = ((SqlExecutionContext) context).getConnection();
                LOG.debug("  save object " + object);
                if (object.getSpecification().isCollectionOrIsAggregated()) {
                    /*
                     * NakedObject parent = object.getSpecification().getAggregate(object);
                     * LOG.debug("change to internal collection being persisted through parent");
                     * 
                     * // TODO a better plan would be ask the mapper to save the collection // -
                     * saveCollection(parent, collection) mapperLookup.getMapper(connection,
                     * parent).save(connection, parent); connectionPool.release(connection);
                     */
                    throw new NotYetImplementedException(object.toString());
                } else {
                    ObjectMapping mapping = objectMappingLookup.getMapping(object);
                    mapping.save(connection, object);
                    connectionPool.release(connection);
                }
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "SaveObjectCommand [object=" + object + "]";
            }

        };
    }

    public void debugData(final DebugString debug) {}

    public String debugTitle() {
        return null;
    }

    public void endTransaction() {}

    public void execute(final List<PersistenceCommand> commands) {
        DatabaseConnector connector = connectionPool.acquire();
        connector.begin();

        NakedObjectTransactionManager transactionManager = NakedObjectsContext.getTransactionManager();
        MessageBroker messageBroker = NakedObjectsContext.getMessageBroker();
        UpdateNotifier updateNotifier = NakedObjectsContext.getUpdateNotifier();
        SqlExecutionContext context = new SqlExecutionContext(connector, transactionManager, messageBroker, updateNotifier);
        try {
            for (PersistenceCommand command : commands) {
                command.execute(context);
            }
            connector.commit();
        } catch (NakedObjectException e) {
            LOG.warn("Failure during execution", e);
            connector.rollback();
            throw e;
        } finally {
            connectionPool.release(connector);
        }
    }

    public boolean flush(final PersistenceCommand[] commands) {
        return false;
    }

    public NakedObject[] getInstances(final PersistenceQuery query) {
        if (query instanceof PersistenceQueryFindByTitle) {
            return getInstancesForTitle((PersistenceQueryFindByTitle) query);
        } else if (query instanceof PersistenceQueryFindAllInstances) {
            return getAllInstances((PersistenceQueryFindAllInstances) query);
        } else if (query instanceof PersistenceQueryFindByPattern)  {
            NakedObject[] allInstances = allInstances(query.getSpecification());
            Vector matches = new Vector();
            for (int i = 0; i < allInstances.length; i++) {
                if (((PersistenceQueryFindByPattern) query).matches(allInstances[i])) {
                    matches.add(allInstances[i]);
                }
            }
            NakedObject[] result = new NakedObject[matches.size()];
            matches.toArray(result);
            return result;
        } else {
            throw new SqlObjectStoreException("Query type not supported: " + query);
        }
    }

    private NakedObject[] getAllInstances(final PersistenceQueryFindAllInstances criteria) {
        NakedObjectSpecification spec = criteria.getSpecification();
        return allInstances(spec);
    }

    private NakedObject[] allInstances(NakedObjectSpecification spec) {
        ObjectMapping mapper = objectMappingLookup.getMapping(spec);
        DatabaseConnector connector = connectionPool.acquire();
        NakedObject[] instances = mapper.getInstances(connector, spec);
        Vector matchingInstances = new Vector();
        for (int i = 0; i < instances.length; i++) {
            matchingInstances.addElement(instances[i]);
        }
        connectionPool.release(connector);
        NakedObject[] instanceArray = new NakedObject[matchingInstances.size()];
        matchingInstances.copyInto(instanceArray);
        return instanceArray;
    }

    private NakedObject[] getInstancesForTitle(final PersistenceQueryFindByTitle criteria) {
        NakedObjectSpecification spec = criteria.getSpecification();
        ObjectMapping mapper = objectMappingLookup.getMapping(spec);

        DatabaseConnector connector = connectionPool.acquire();
        NakedObject[] instances = mapper.getInstances(connector, spec);
        Vector matchingInstances = new Vector();
        for (int i = 0; i < instances.length; i++) {
            if (criteria.matches(instances[i])) {
                matchingInstances.addElement(instances[i]);
            }
        }
        connectionPool.release(connector);

        NakedObject[] instanceArray = new NakedObject[matchingInstances.size()];
        matchingInstances.copyInto(instanceArray);
        return instanceArray;
    }

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) {
        ObjectMapping mapper = objectMappingLookup.getMapping(hint);
        DatabaseConnector connection = connectionPool.acquire();
        NakedObject object = mapper.getObject(connection, oid, hint);
        connectionPool.release(connection);
        return object;
    }

    public Oid getOidForService(String name) {
        DatabaseConnector connector = connectionPool.acquire();

        Results results = connector.select("select PK_ID from NO_SERVICES where ID = '" + name + "'");
        if (results.next()) {
            int key = results.getInt("PK_ID");
            connectionPool.release(connector);
            return SqlOid.createPersistent(name, new IntegerPrimaryKey(key));
        } else {
            connectionPool.release(connector);
            return null;
        }
    }

    public boolean hasInstances(final NakedObjectSpecification spec) {
        DatabaseConnector connection = connectionPool.acquire();
        ObjectMapping mapper = objectMappingLookup.getMapping(spec);
        boolean hasInstances = mapper.hasInstances(connection, spec);
        connectionPool.release(connection);
        return hasInstances;
    }

    public boolean isFixturesInstalled() {
        return isInitialized;
    }

    public void open() {
        objectMappingLookup.init();

        DatabaseConnector connector = connectionPool.acquire();
        isInitialized = connector.hasTable("NO_SERVICES");
        if (!isInitialized) {
            connector.update("create table NO_SERVICES (PK_ID int, ID varchar(255))");
        }
    }

    public String name() {
        return "SQL Object Store";
    }

    public void registerService(final String name, final Oid oid) {
        DatabaseConnector connector = connectionPool.acquire();
        String soid = ((SqlOid) oid).getPrimaryKey().stringValue();
        connector.insert("insert into NO_SERVICES (PK_ID, ID) values ( " + soid + ", '" + name + "')");
        connectionPool.release(connector);
    }

    public void reset() {}

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) {
        if (field.isOneToManyAssociation()) {
            DatabaseConnector connection = connectionPool.acquire();
            NakedObjectSpecification spec = object.getSpecification();
            ObjectMapping mapper = objectMappingLookup.getMapping(spec);
            mapper.resolveCollection(connection, object, field);
            connectionPool.release(connection);
        } else {
            resolveImmediately(field.get(object));
        }
    }

    public void resolveImmediately(final NakedObject object) {
        DatabaseConnector connector = connectionPool.acquire();
        ObjectMapping mapping = objectMappingLookup.getMapping(object);
        mapping.resolve(connector, object);
        connectionPool.release(connector);
    }

    public void setConnectionPool(final DatabaseConnectorPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void setMapperLookup(final ObjectMappingLookup mapperLookup) {
        this.objectMappingLookup = mapperLookup;
    }

    public void close() {
        objectMappingLookup.shutdown();
        // DatabaseConnector connection = connectionPool.acquire();
        // connection.update("SHUTDOWN");
        connectionPool.shutdown();
    }

    public void startTransaction() {}

}
// Copyright (c) Naked Objects Group Ltd.
