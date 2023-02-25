package org.nakedobjects.runtime.system;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.specloader.internal.OneToManyAssociationImpl;
import org.nakedobjects.runtime.system.specpeer.DummyOneToManyPeer;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestSpecification;



public class OneToManyAssociationImplTest extends ProxyJunit3TestCase {
    private static final String FIELD_ID = "members";
    private static final String FIELD_NAME = "Members";
    private NakedObject nakedObject;
    private NakedObject associate;
    private OneToManyAssociation association;
    private TestSpecification type;
    private DummyOneToManyPeer associationDelegate;
	private RuntimeContext runtimeContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        nakedObject = system.createPersistentTestObject();
        associate = system.createPersistentTestObject();
        runtimeContext = new RuntimeContextNoRuntime();

        associationDelegate = new DummyOneToManyPeer(system.getSpecification(String.class));
        association = new OneToManyAssociationImpl(associationDelegate, runtimeContext);
    }

    public void xxxtestType() {
        assertEquals(type, association.getSpecification());
    }

    public void xxxtestSet() {
        association.addElement(nakedObject, associate);
        associationDelegate.assertAction(0, "add " + nakedObject);
        associationDelegate.assertAction(1, "add " + associate);
    }

    public void xxxtestClear() {
        association.removeElement(nakedObject, associate);
        associationDelegate.assertAction(0, "remove " + nakedObject);
        associationDelegate.assertAction(1, "remove " + associate);
    }

    public void testClearWithNull() {
        try {
            association.removeElement(nakedObject, null);
            fail();
        } catch (final IllegalArgumentException expected) {}
        associationDelegate.assertActions(0);
    }

    public void testSetWithNull() {
        try {
            association.addElement(nakedObject, null);
            fail();
        } catch (final IllegalArgumentException expected) {}
        associationDelegate.assertActions(0);
    }

    public void xxxtestName() {
        assertEquals(FIELD_NAME, association.getId());
    }

    public void xxxtestLabel() {
        assertEquals(FIELD_NAME, association.getName());
    }
}
// Copyright (c) Naked Objects Group Ltd.
