package org.nakedobjects.runtime.persistence.objectstore;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.factory.InstanceCreationException;
import org.nakedobjects.metamodel.commons.logging.Logger;
import org.nakedobjects.metamodel.config.ConfigurationException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.UnsupportedFindException;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class NakedObjectStoreLogger extends Logger implements ObjectStore {
    private final ObjectStore decorated;

    public NakedObjectStoreLogger(final ObjectStore decorated, final String logFileName) {
        super(logFileName, false);
        this.decorated = decorated;
    }

    public NakedObjectStoreLogger(final ObjectStore decorated) {
        super(null, true);
        this.decorated = decorated;
    }

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        log("Create object " + object);
        return decorated.createCreateObjectCommand(object);
    }

    public void registerService(final String name, final Oid oid) {
        log("register service " + name + " as " + oid);
        decorated.registerService(name, oid);
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        log("Destroy object " + object);
        return decorated.createDestroyObjectCommand(object);
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        log("Save object " + object);
        return decorated.createSaveObjectCommand(object);
    }

    public void debugData(final DebugString debug) {
        decorated.debugData(debug);
    }

    public String debugTitle() {
        return decorated.debugTitle();
    }

    @Override
    protected Class<?> getDecoratedClass() {
        return decorated.getClass();
    }

    public NakedObject[] getInstances(final PersistenceQuery criteria) throws ObjectPersistenceException,
            UnsupportedFindException {
        log("Get instances matching " + criteria);
        return decorated.getInstances(criteria);
    }

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException,
            ObjectPersistenceException {
        final NakedObject object = decorated.getObject(oid, hint);
        log("Get object for " + oid + " (of type " + hint.getShortName() + ")", object.getObject());
        return object;
    }

    public Oid getOidForService(final String name) {
        final Oid oid = decorated.getOidForService(name);
        log("Get OID for service " + name + ": " + oid);
        return oid;
    }

    public boolean hasInstances(final NakedObjectSpecification specification)
            throws ObjectPersistenceException {
        final boolean hasInstances = decorated.hasInstances(specification);
        log("Has instances of " + specification.getShortName(), "" + hasInstances);
        return hasInstances;
    }

    public boolean isFixturesInstalled() {
        final boolean isInitialized = decorated.isFixturesInstalled();
        log("is initialized: " + isInitialized);
        return isInitialized;
    }

    public void open() throws ConfigurationException, InstanceCreationException, ObjectPersistenceException {
        log("Opening " + name());
        decorated.open();
    }

    public String name() {
        return decorated.name();
    }

    public void reset() {
        log("Reset");
        decorated.reset();
    }

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) throws ObjectPersistenceException {
        log("Resolve eagerly object in field " + field + " of " + object);
        decorated.resolveField(object, field);
    }

    public void resolveImmediately(final NakedObject object) throws ObjectPersistenceException {
        log("Resolve immediately: " + object);
        decorated.resolveImmediately(object);
    }

    public void execute(final List<PersistenceCommand> commands) throws ObjectPersistenceException {
        log("Execute commands");
        int i = 0;
        for (PersistenceCommand command: commands) {
            log("  " + (i++) + " " + command);
        }
        decorated.execute(commands);
    }

    public void close() throws ObjectPersistenceException {
        log("Closing " + decorated);
        decorated.close();
    }

    public void startTransaction() {
        decorated.startTransaction();
    }

    public void endTransaction() {
        decorated.endTransaction();
    }
    
    public void abortTransaction() {
        decorated.abortTransaction();
    }

}
// Copyright (c) Naked Objects Group Ltd.
