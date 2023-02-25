package org.nakedobjects.progmodel.java5.reflect;

import java.lang.reflect.Method;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet.Where;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.progmodel.java5.facets.actions.invoke.JavaActionInvocationFacet;
import org.nakedobjects.progmodel.java5.facets.actions.validate.ActionValidMethodFacet;
import org.nakedobjects.progmodel.java5.reflect.actions.JavaAction;
import org.nakedobjects.progmodel.java5.reflect.actions.JavaActionParam;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.ProxyTestCase;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;


public class JavaActionTest extends ProxyTestCase {
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(JavaActionTest.class);
    }

    private JavaAction javaAction;
    private TestProxyNakedObject target;
    private JavaActionTestObject javaObject;

    protected void setUp() throws Exception {
        super.setUp();

        javaObject = new JavaActionTestObject();
        target = system.createAdapterForTransient(javaObject);

        MemberIdentifierImpl memberIdentifierImpl = new MemberIdentifierImpl("cls", "methodName",
                (NakedObjectSpecification[]) null);
        javaAction = new JavaAction(memberIdentifierImpl, null, null, new JavaActionParam[0]);
        assertNotNull(javaAction);

        Class cls = JavaActionTestObject.class;
        Method action = cls.getDeclaredMethod("actionMethod", new Class[0]);
        javaAction.addFacet(new JavaActionInvocationFacet(action, null, javaAction));

        Method valid = cls.getDeclaredMethod("validMethod", new Class[0]);
        javaAction.addFacet(new ActionValidMethodFacet(valid, javaAction));

    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testAction() throws Exception {
        javaAction.execute(target, new Naked[0]);
        assertTrue(javaObject.actionCalled());
    }

    public void testReturnType() {
        assertNull(javaAction.getReturnType());
    }

    public void testTypeUserByDefault() {
        assertEquals(NakedObjectAction.USER, javaAction.getType());
    }

    public void testTypeExploration() {
        javaAction.addFacet(new ExplorationFacet(javaAction));
        assertEquals(NakedObjectAction.EXPLORATION, javaAction.getType());
    }

    public void testTypeDebug() {
        javaAction.addFacet(new DebugFacet(javaAction));
        assertEquals(NakedObjectAction.DEBUG, javaAction.getType());
    }

    public void testTargetDefault() {
        javaAction.addFacet(new DebugFacet(javaAction));
        assertEquals(NakedObjectAction.DEFAULT, javaAction.getTarget());
    }

    public void testTargetLocal() {
        javaAction.addFacet(new ExecutedFacet(Where.LOCALLY, javaAction));
        assertEquals(NakedObjectAction.LOCAL, javaAction.getTarget());
    }

    public void testTargetRemote() {
        javaAction.addFacet(new ExecutedFacet(Where.REMOTELY, javaAction));
        assertEquals(NakedObjectAction.REMOTE, javaAction.getTarget());
    }

    public void testUsableByDefault() {
        assertEquals(Allow.DEFAULT, javaAction.isUsableDeclaratively());
    }

    public void testUsableDeclaratively() {
        javaAction.addFacet(new DisabledFacet(When.ALWAYS, javaAction));
        assertTrue(javaAction.isUsableDeclaratively().isVetoed());
    }

    public void testUsableInContextByDefault() {
        assertEquals(Allow.DEFAULT, javaAction.isUsable(target));
    }

    public void testTransientObjectDisabled() {
        javaAction.addFacet(new DisabledFacet(When.UNTIL_PERSISTED, javaAction));
        assertTrue(javaAction.isUsable(target).isVetoed());
    }

    public void testTransientObjectEnabled() {
        javaAction.addFacet(new DisabledFacet(When.ONCE_PERSISTED, javaAction));
        assertEquals(Allow.DEFAULT, javaAction.isUsable(target));
    }

    public void testPersistentObjectDisabled() {
        system.makePersistent(target);
        javaAction.addFacet(new DisabledFacet(When.ONCE_PERSISTED, javaAction));
        assertTrue(javaAction.isUsable(target).isVetoed());
    }

    public void testPesistentObjectEnabled() {
        system.makePersistent(target);
        javaAction.addFacet(new DisabledFacet(When.UNTIL_PERSISTED, javaAction));
        assertEquals(Allow.DEFAULT, javaAction.isUsable(target));
    }

    public void testParameterSetValid() throws Exception {
        Consent consent = javaAction.isParameterSetValidImperatively(target, new Naked[0]);
        assertEquals(false, consent.isAllowed());
        assertEquals("invalid", consent.getReason());
    }
}
// Copyright (c) Naked Objects Group Ltd.
