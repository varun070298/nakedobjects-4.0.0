package org.nakedobjects.example.objectstore;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.example.domain.Customer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class ExampleObjectStore implements ObjectStore {
    private static final Logger LOG = Logger.getLogger(ExampleObjectStore.class);

    // @extract createDatabase
    public static void main(String[] args) {
        JdbcManager jdbcManager = new JdbcManager();
        String createTable = "CREATE TABLE customer ( id INTEGER IDENTITY, name VARCHAR(256), version INTEGER)";
        jdbcManager.execute(createTable);
        jdbcManager.close();
    }
    // @extract-end

    private final NakedObjectConfiguration configuration;
    private JdbcManager jdbcManager;

    public ExampleObjectStore(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new CreateObjectCommand() {

            // @extract addCommand
            public void execute(NakedObjectTransaction transaction) throws ObjectPersistenceException {
                LOG.debug("  create object " + object);
                String user = NakedObjectsContext.getAuthenticationSession().getUserName();
                SqlVersion version = new SqlVersion(user);
                object.setOptimisticLock(version);
                String insertCustomer = "INSERT into customer (id, name, version) values (1, '"
                        + ((Customer) object.getObject()).getName() + "', " + version.sequence() + ")";
                jdbcManager.execute(insertCustomer);
            }
            // @extract-end

            public NakedObject onObject() {
                return object;
            }

            @Override
            public String toString() {
                return "CreateObjectCommand [object=" + object + "]";
            }
        };
    }

    public DestroyObjectCommand createDestroyObjectCommand(NakedObject object) {
        return null;
    }

    public SaveObjectCommand createSaveObjectCommand(NakedObject object) {
        return null;
    }

    public void execute(List<PersistenceCommand> commands) {
        for (PersistenceCommand command : commands) {
            command.execute(null);
        }
    }

    public NakedObject[] getInstances(PersistenceQuery criteria) {
        return null;
    }

    public NakedObject getObject(Oid oid, NakedObjectSpecification hint) {
        return null;
    }

    public Oid getOidForService(String name) {
        return null;
    }

    public boolean hasInstances(NakedObjectSpecification specification) {
        return false;
    }

    public boolean isFixturesInstalled() {
        return false;
    }

    public void registerService(String name, Oid oid) {}

    public void reset() {}

    public void resolveField(NakedObject object, NakedObjectAssociation field) {}

    public void resolveImmediately(NakedObject object) {}

    public void debugData(DebugString debug) {}

    public String debugTitle() {
        return null;
    }

    public void close() {}

    public void open() {
        if (jdbcManager == null) {
            jdbcManager = new JdbcManager();
        }
        LOG.info("open");

    }

    public String name() {
        return null;
    }

    public void abortTransaction() {}

    public void endTransaction() {}

    public void startTransaction() {}

}

// Copyright (c) Naked Objects Group Ltd.
