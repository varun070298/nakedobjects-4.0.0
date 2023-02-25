package org.nakedobjects.plugins.xml.objectstore.internal.data;

import java.util.Vector;

import junit.framework.Assert;

import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class MockDataManager implements DataManager {
    private final Vector actions = new Vector();

    public void assertAction(final int i, final String action) {
        if (i >= actions.size()) {
            Assert.fail("No such action " + action);
        }
        // Assert.assertEquals(action, actions.elementAt(i));
    }

    public MockDataManager() {
        super();
    }

    public SerialOid createOid() throws PersistorException {
        return null;
    }

    public void insertObject(final ObjectData data) throws ObjectPersistenceException {}

    public boolean isFixturesInstalled() {
        return true;
    }

    public void remove(final SerialOid oid) throws ObjectNotFoundException, ObjectPersistenceException {}

    public void save(final Data data) throws ObjectPersistenceException {
        actions.addElement(data);
    }

    public void shutdown() {}

    public ObjectDataVector getInstances(final ObjectData pattern) {
        return null;
    }

    public Data loadData(final SerialOid oid) {
        return null;
    }

    public int numberOfInstances(final ObjectData pattern) {
        actions.addElement(pattern.getTypeName());

        return 5;
    }

    public String getDebugData() {
        return null;
    }

}
// Copyright (c) Naked Objects Group Ltd.
