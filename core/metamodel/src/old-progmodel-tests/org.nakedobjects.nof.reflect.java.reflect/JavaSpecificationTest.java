package org.nakedobjects.progmodel.java5.reflect;

import org.nakedobjects.noa.adapter.Persistable;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.reflect.spec.AbstractSpecification;
import org.nakedobjects.nof.reflect.spec.ReflectionPeerBuilder;
import org.nakedobjects.nof.testsystem.ProxyTestCase;


public class JavaSpecificationTest extends ProxyTestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(JavaSpecificationTest.class);
    }

    private ReflectionPeerBuilder builder = new DummyBuilder();

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testNoFlag() throws Exception {
        AbstractSpecification spec = new JavaSpecification(TestObject.class, new DummyBuilder(), new JavaReflector());
        spec.introspect(builder);
        assertEquals(0, spec.getFeatures());
    }

    public void testNotPersistable() throws Exception {
        AbstractSpecification spec = new JavaSpecification(JavaObjectMarkedAsTransient.class, new DummyBuilder(),
                new JavaReflector());
        spec.introspect(builder);
        assertEquals(Persistable.TRANSIENT, spec.persistable());

    }

    public void testPersistable() throws Exception {
        AbstractSpecification spec = new JavaSpecification(JavaObjectWithBasicProgramConventions.class, new DummyBuilder(), new JavaReflector());
        spec.introspect(builder);
        assertEquals(Persistable.USER_PERSISTABLE, spec.persistable());
    }

    public void testService() throws Exception {
        AbstractSpecification spec = new JavaSpecification(TestObjectAsService.class, new DummyBuilder(), new JavaReflector());
        spec.introspect(builder);
        spec.markAsService();
        assertEquals(NakedObjectSpecification.SERVICE, spec.getFeatures());
    }
}
// Copyright (c) Naked Objects Group Ltd.
