package test.org.nakedobjects.object.security;

import junit.framework.TestCase;

import org.nakedobjects.nof.core.context.StaticContext;
import org.nakedobjects.testing.NullSession;

import test.org.nakedobjects.object.reflect.DummyActionPeer;


public class ActionAuthorisationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ActionAuthorisationTest.class);
    }

    private MockAuthorisationManager manager;
    private ActionAuthorisation action;

    protected void setUp() throws Exception {
        StaticContext.createInstance();
        DummyActionPeer peer = new DummyActionPeer();
        manager = new MockAuthorisationManager();
        action = new ActionAuthorisation(peer, manager);
    }

    public void testIsAccessible() {
        manager.setupUsable(true);
        manager.setupVisible(true);

        assertTrue(action.isVisibleForSession(new NullSession()));
    }

    public void testIsNotAccessible() {
        manager.setupUsable(true);
        manager.setupVisible(false);

        assertFalse(action.isVisibleForSession(new NullSession()));
    }
}
// Copyright (c) Naked Objects Group Ltd.
