package org.nakedobjects.runtime.persistence.objectstore.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.runtime.persistence.adapterfactory.pojo.PojoAdapter;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;


public abstract class PersistAlgorithmContractTest extends ProxyJunit3TestCase {

    protected final class PersistedObjectAdderSpy implements ToPersistObjectSet {
        private final List<NakedObject> persistedObjects = new ArrayList<NakedObject>();

        public List<NakedObject> getPersistedObjects() {
            return persistedObjects;
        }

        public void addPersistedObject(final NakedObject object) {
            persistedObjects.add(object);
        }

        public void remapAsPersistent(final NakedObject object) {
            object.changeState(ResolveState.RESOLVED);
        }
    }

    interface PersistAlgorithmSensing extends PersistAlgorithm {
        void persist(NakedObject object, ToPersistObjectSet adder);
    }

    private PersistedObjectAdderSpy adder;
    private PersistAlgorithm persistAlgorithm;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        adder = new PersistedObjectAdderSpy();
        persistAlgorithm = createPersistAlgorithm();
    }

    /**
     * Hook for any implementation to implement.
     * 
     * @return
     */
    protected abstract PersistAlgorithm createPersistAlgorithm();

    public void testMakePersistentSkipsAggregatedObjects() {
        final PojoAdapter aggregatedObject = new PojoAdapter(new Object(), SerialOid.createTransient(1));
        aggregatedObject.changeState(ResolveState.VALUE);
        persistAlgorithm.makePersistent(aggregatedObject, adder);
        assertEquals(0, adder.getPersistedObjects().size());
    }

}

// Copyright (c) Naked Objects Group Ltd.
