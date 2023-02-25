package test.org.nakedobjects.object.security;

import junit.framework.TestCase;

import org.nakedobjects.nof.core.context.StaticContext;
import org.nakedobjects.nof.reflect.security.OneToOneAuthorisation;
import org.nakedobjects.testing.NullSession;

import test.org.nakedobjects.object.reflect.DummyOneToOnePeer;


public class OneToOneAuthorisationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(OneToOneAuthorisationTest.class);

    }

    private MockAuthorisationManager manager;
    private OneToOneAuthorisation oneToOne;

    protected void setUp() throws Exception {
        StaticContext.createInstance();

        DummyOneToOnePeer peer = new DummyOneToOnePeer();
        manager = new MockAuthorisationManager();
        oneToOne = new OneToOneAuthorisation(peer, manager);
    }

    public void testGetHint() {

        manager.setupUsable(true);
        manager.setupVisible(true);

        assertTrue(oneToOne.isVisibleForSession(new NullSession()));
    }
}
// Copyright (c) Naked Objects Group Ltd.
