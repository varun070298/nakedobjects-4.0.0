package test.org.nakedobjects.object.security;

import junit.framework.TestCase;

import org.nakedobjects.nof.core.context.StaticContext;
import org.nakedobjects.nof.reflect.security.OneToManyAuthorisation;
import org.nakedobjects.testing.NullSession;

import test.org.nakedobjects.object.reflect.DummyOneToManyPeer;


public class OneToManyAuthorisationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(OneToManyAuthorisationTest.class);
    }

    private MockAuthorisationManager manager;
    private OneToManyAuthorisation oneToMany;

    protected void setUp() throws Exception {
        StaticContext.createInstance();

        DummyOneToManyPeer peer = new DummyOneToManyPeer();
        manager = new MockAuthorisationManager();
        oneToMany = new OneToManyAuthorisation(peer, manager);
    }

    public void testAccessible() {
        manager.setupUsable(true);
        manager.setupVisible(true);

        assertTrue(oneToMany.isVisibleForSession(new NullSession()));
    }

    public void testNotAccessible() {
        manager.setupUsable(true);
        manager.setupVisible(false);

        assertFalse(oneToMany.isVisibleForSession(new NullSession()));
    }
}
// Copyright (c) Naked Objects Group Ltd.
