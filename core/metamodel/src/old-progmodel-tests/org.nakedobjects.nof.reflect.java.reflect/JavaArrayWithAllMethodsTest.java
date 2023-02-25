package org.nakedobjects.progmodel.java5.reflect;

import java.lang.reflect.Method;

import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.progmodel.java5.method.JavaBeanPropertyAccessorMethod;
import org.nakedobjects.progmodel.java5.reflect.collections.JavaCollectionAssociation;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.TestProxySystem;
import org.nakedobjects.testing.DummyNakedCollection;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;

import test.org.nakedobjects.object.NakedObjectTestCase;


public class JavaArrayWithAllMethodsTest extends NakedObjectTestCase {
    private static final String MEMBERS_FIELD_NAME = "members";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(JavaArrayWithAllMethodsTest.class));
    }

    private JavaCollectionAssociation collectionField;
    private JavaReferencedObject elements[];
    private NakedObject nakedObject;
    private JavaObjectWithVector objectWithVector;
    private TestProxySystem system;

    public JavaArrayWithAllMethodsTest(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        system = new TestProxySystem();
        system.init();

        objectWithVector = new JavaObjectWithVector();
        nakedObject = system.createAdapterForTransient(objectWithVector);
        elements = new JavaReferencedObject[3];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new JavaReferencedObject();
        }

        Class cls = JavaObjectWithVector.class;
        MemberIdentifierImpl memberIdentifierImpl = new MemberIdentifierImpl("cls", MEMBERS_FIELD_NAME);
        collectionField = new JavaCollectionAssociation(memberIdentifierImpl, Object.class);

        Method get = cls.getDeclaredMethod("getMethod", new Class[0]);
		collectionField.addFacet(new JavaBeanPropertyAccessorMethod(get, collectionField));
        
        Method add = cls.getDeclaredMethod("addToMethod", new Class[] { JavaReferencedObject.class });
        Method remove = cls.getDeclaredMethod("removeFromMethod", new Class[] { JavaReferencedObject.class });

        Method visible = cls.getDeclaredMethod("hideMethod", new Class[] { JavaReferencedObject.class });
        Method available = cls.getDeclaredMethod("availableMethod", new Class[] { JavaReferencedObject.class });
        Method valid = cls.getDeclaredMethod("validMethod", new Class[] { JavaReferencedObject.class });
/*
        FieldMethods fieldMethods = new FieldMethods(get, null, add, remove, null, null);
        GeneralControlMethods controlMethods = new GeneralControlMethods(new MethodHelper(visible), new MethodHelper(available), valid, null);
        DescriptiveMethods descriptiveMethods = new DescriptiveMethods(new StaticHelper("name"), null);
        FieldFlags xxMethods = new FieldFlags(When.NEVER, false, When.NEVER, false, "");
        FieldSessionMethods sessionMethods = new FieldSessionMethods(null, null);
*/
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testAdd() {
        JavaReferencedObject associate = new JavaReferencedObject();
        NakedObject nakedObjectAssoicate = system.createAdapterForTransient(associate);

//        spec = new TestSpecification();
//        system.addSpecificationToLoader(spec);

        assertNull(objectWithVector.added);
        collectionField.addAssociation(nakedObject, nakedObjectAssoicate);
        assertEquals(associate, objectWithVector.added);
    }

    public void testAvailableWhenModifiableAndAvailable() throws Exception {
        objectWithVector.available = true;
        assertTrue(collectionField.isUsable(nakedObject).isAllowed());
    }

    public void testGet() {
        TestSpecification spec = new TestSpecification();
        system.addSpecificationToLoader(spec);

        spec = new TestSpecification();
        system.addSpecificationToLoader(spec);

        DummyNakedCollection collection = new DummyNakedCollection();
        system.addCollectionToObjectLoader(collection);

        assertNotNull(collectionField.getAssociations(nakedObject));
        assertEquals(collection.getObject(), collectionField.getAssociations(nakedObject).getObject());
    }

    public void testNotAvailableWhenModifiable() throws Exception {
        objectWithVector.available = false;
        assertFalse(collectionField.isUsable(nakedObject).isAllowed());
        assertEquals("not available", collectionField.isUsable(nakedObject).getReason());
    }

    public void testRemove() {
        TestSpecification spec = new TestSpecification();
        system.addSpecificationToLoader(spec);

        JavaReferencedObject associate = new JavaReferencedObject();
        NakedObject nakedObjectAssoicate = system.createAdapterForTransient(associate);

        spec = new TestSpecification();
        system.addSpecificationToLoader(spec);

        assertNull(objectWithVector.removed);
        collectionField.removeAssociation(nakedObject, nakedObjectAssoicate);
        assertEquals(associate, objectWithVector.removed);
    }

    public void testType() {
        TestSpecification spec = new TestSpecification(NakedCollection.class.getName());
        system.addSpecificationToLoader(spec);
        assertEquals(spec, collectionField.getSpecification());
    }

    public void testValid() {
        assertFalse(collectionField.isAddValid(nakedObject, null).isAllowed());
    }

    public void testVisible() {
        assertFalse("invisible", collectionField.isVisible(nakedObject));
        objectWithVector.visible = true;
        assertTrue("visible", collectionField.isVisible(nakedObject));
    }
}
// Copyright (c) Naked Objects Group Ltd.
