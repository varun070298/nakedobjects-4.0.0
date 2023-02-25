package org.nakedobjects.runtime.persistence.objectstore.algorithm.dflt;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.runtime.persistence.NotPersistableException;
import org.nakedobjects.runtime.persistence.adapterfactory.pojo.PojoAdapter;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testspec.OneToOneAssociationTest;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;



public class DefaultPersistAlgorithmTest extends ProxyJunit3TestCase {

    private final class PersistedObjectAdderSpy implements ToPersistObjectSet {
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

    private DefaultPersistAlgorithm algorithm;
    private PersistedObjectAdderSpy adder;
    private NakedObject object;
    private TestProxyNakedObject fieldsObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        algorithm = new DefaultPersistAlgorithm();

        object = system.createTransientTestObject();
        // object.setupResolveState(ResolveState.TRANSIENT);

        final TestProxySpecification spec = system.getSpecification(object);
        final NakedObjectAssociation[] fields = new NakedObjectAssociation[] { new OneToOneAssociationTest() {

            public void initAssociation(NakedObject inObject, NakedObject associate) {}

            public Consent isAssociationValid(NakedObject inObject, NakedObject associate) {
                return null;
            }

            public void setAssociation(NakedObject inObject, NakedObject associate) {}

            public void set(NakedObject owner, NakedObject newValue) {}

            public NakedObject get(NakedObject target) {
                return null;
            }

            public NakedObjectSpecification getSpecification() {
                return null;
            }

            public String debugData() {
                return null;
            }

            public String getId() {
                return null;
            }

            public String getName() {
                return null;
            }

        } };
        spec.setupFields(fields);

        fieldsObject = new TestProxyNakedObject();
        fieldsObject.setupResolveState(ResolveState.TRANSIENT);
        fieldsObject.setupSpecification(system.getSpecification(String.class));

        adder = new PersistedObjectAdderSpy();
    }

    public void testMakePersistentFailsIfObjectAlreadyPersistent() {
        object.changeState(ResolveState.RESOLVED);
        try {
            algorithm.makePersistent(object, adder);
            fail();
        } catch (final NotPersistableException expected) {}
    }

    public void testMakePersistentFailsIfObjectMustBeTransient() {
        try {
            system.getSpecification(object).setupPersistable(Persistability.TRANSIENT);
            algorithm.makePersistent(object, adder);
        } catch (final NotPersistableException expected) {}
    }

    public void testMakePersistent() {
        algorithm.makePersistent(object, adder);
        assertEquals(ResolveState.RESOLVED, object.getResolveState());
        assertTrue(adder.getPersistedObjects().contains(object));
    }

    public void testMakePersistentRecursesThroughReferenceFields() {
        /*
         * fieldControl.expectAndReturn(oneToOneAssociation.isPersisted(), true);
         * fieldControl.expectAndReturn(oneToOneAssociation.isValue(), false);
         * fieldControl.expectAndReturn(oneToOneAssociation.get(object), fieldsObject);
         * NakedObjectsContext.getObjectPersistor().getIdentityMap().madePersistent(object);
         * NakedObjectsContext.getObjectPersistor().getIdentityMap().madePersistent(fieldsObject);
         * 
         * adder.addPersistedObject(object); adder.addPersistedObject(fieldsObject);
         */

        // replay();
        algorithm.makePersistent(object, adder);
        // verify();
    }

    public void testMakePersistentRecursesThroughReferenceFieldsSkippingNullReferences() {
        /*
         * fieldControl.expectAndReturn(oneToOneAssociation.isPersisted(), true);
         * fieldControl.expectAndReturn(oneToOneAssociation.isValue(), false);
         * fieldControl.expectAndReturn(oneToOneAssociation.get(object), null);
         * 
         * NakedObjectsContext.getObjectPersistor().getIdentityMap().madePersistent(object);
         * 
         * adder.addPersistedObject(object);
         */
        algorithm.makePersistent(object, adder);
    }

    public void testMakePersistentRecursesThroughReferenceFieldsSkippingNonPersistentFields() {
        /*
         * fieldControl.expectAndReturn(oneToOneAssociation.isPersisted(), false);
         * 
         * NakedObjectsContext.getObjectPersistor().getIdentityMap().madePersistent(object);
         * 
         * adder.addPersistedObject(object);
         */
        algorithm.makePersistent(object, adder);
    }

    public void testMakePersistentRecursesThroughReferenceFieldsSkippingObjectsThatAreAlreadyPersistent() {
        /*
         * fieldControl.expectAndReturn(oneToOneAssociation.isPersisted(), true);
         * fieldControl.expectAndReturn(oneToOneAssociation.isValue(), false);
         * fieldControl.expectAndReturn(oneToOneAssociation.get(object), fieldsObject);
         * fieldsObject.setupResolveState(ResolveState.RESOLVED);
         * 
         * NakedObjectsContext.getObjectPersistor().getIdentityMap().madePersistent(object);
         * 
         * adder.addPersistedObject(object);
         */
        algorithm.makePersistent(object, adder);
    }

    public void testMakePersistentSkipsAggregatedObjects() {
        class DefaultPersistAlgorithmSubclassForTesting extends DefaultPersistAlgorithm {
            @Override
            protected void persist(final NakedObject object, final ToPersistObjectSet persistor) {
                super.persist(object, persistor);
            }

            public void sensingPersist(final NakedObject object, final ToPersistObjectSet persistor) {
                persist(object, persistor);
            }
        }
        final PojoAdapter aggregatedObject = new PojoAdapter(new Object(), SerialOid.createTransient(1));
        aggregatedObject.changeState(ResolveState.VALUE);
        new DefaultPersistAlgorithmSubclassForTesting().sensingPersist(aggregatedObject, adder);
        assertEquals(0, adder.getPersistedObjects().size());
    }

}

// Copyright (c) Naked Objects Group Ltd.
