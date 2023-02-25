package org.nakedobjects.runtime.persistence.objectstore;

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.UnsupportedFindException;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class ObjectStoreSpy implements ObjectStore, ObjectStoreTransactionManagement {

    private final Vector<String> actions = new Vector<String>();
    @SuppressWarnings("unused")
    private int instanceCount;
    @SuppressWarnings("unused")
    private NakedObjectSpecification expectedClass;
    private NakedObject getObject;
    private NakedObject[] instances = null;
    private boolean hasInstances;

    public ObjectStoreSpy() {
        super();
    }

    
    public void open() throws ObjectPersistenceException {}

    public void close() {}


    public boolean isFixturesInstalled() {
        return true;
    }

    public void reset() {
        instanceCount = 0;
        actions.removeAllElements();
    }

    
    public void assertAction(final int i, final String expected) {
        Assert.assertTrue("invalid action number " + i, actions.size() > i);
        final String actual = actions.elementAt(i);

        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && actual.startsWith(expected)) {
            return;
        }
        Assert.fail("action " + i + " expected: <" + expected + "> but was: <" + actual + ">");
    }

    public void assertLastAction(final int expectedLastAction) {
        final int actualLastAction = actions.size() - 1;
        Assert.assertEquals(expectedLastAction, actualLastAction);
    }

    public void abortTransaction() {
        actions.addElement("abortTransaction");
    }

    public void setupGetObject(final NakedObject object) {
        getObject = object;
    }

    public void createNakedClass(final NakedObject cls) throws ObjectPersistenceException {}

    public void endTransaction() {
        actions.addElement("endTransaction");
    }

    public Vector<String> getActions() {
        return actions;
    }

    public void debugData(final DebugString debug) {}

    public String debugTitle() {
        return null;
    }

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException,
            ObjectPersistenceException {
        if (getObject == null) {
            Assert.fail("no object expected");
        }
        Assert.assertEquals(getObject.getOid(), oid);
        return getObject;
    }

    public Oid getOidForService(final String name) {
        return null;
    }


    public void setupHasInstances(final boolean flag) {
        hasInstances = flag;
    }

    public boolean hasInstances(final NakedObjectSpecification cls) {
        return hasInstances;
    }

    
    public String name() {
        return null;
    }

    public void resolveImmediately(final NakedObject object) {}

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) throws ObjectPersistenceException {}

    public void setupInstancesCount(final int i) {
        instanceCount = i;
    }

    public void setupInstances(final NakedObject[] instances, final NakedObjectSpecification cls) {
        this.instances = instances;
        this.expectedClass = cls;
    }

    public void startTransaction() {
        actions.addElement("startTransaction");
    }

    public NakedObject[] getInstances(final PersistenceQuery criteria) throws ObjectPersistenceException,
            UnsupportedFindException {
        actions.addElement("getInstances " + criteria);
        return instances;
    }

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        actions.addElement("createObject " + object);
        return new CreateObjectCommand() {

            public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {}

            @Override
            public String toString() {
                return "CreateObjectCommand " + object.toString();
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        actions.addElement("destroyObject " + object);
        return new DestroyObjectCommand() {

            public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {}

            @Override
            public String toString() {
                return "DestroyObjectCommand " + object.toString();
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        actions.addElement("saveObject " + object);
        return new SaveObjectCommand() {

            public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {}

            @Override
            public String toString() {
                return "DestroyObjectCommand " + object.toString();
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public void execute(final List<PersistenceCommand> commands) throws ObjectPersistenceException {
        for (PersistenceCommand command: commands) {
            actions.addElement("execute " + command);
            command.execute(null);
        }
    }

    public void registerService(final String name, final Oid oid) {}

}
// Copyright (c) Naked Objects Group Ltd.
