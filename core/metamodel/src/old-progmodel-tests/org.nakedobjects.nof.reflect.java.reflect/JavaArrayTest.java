package org.nakedobjects.progmodel.java5.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.nof.core.util.UnexpectedCallException;
import org.nakedobjects.progmodel.java5.method.JavaBeanPropertyAccessorMethod;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.TestPojo;
import org.nakedobjects.nof.testsystem.TestProxyNakedCollection;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;
import org.nakedobjects.nof.testsystem.TestProxySystem;


public class JavaArrayTest extends TestCase {

    private TestProxySystem system;
    private JavaCollectionAssociation listField;
    private TestObjectWithCollection testPojo;
    private TestProxyNakedObject testAdapter;
    private Object[] collection;

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        system = new TestProxySystem();
        system.init();

//        Class cls = TestPojo.class;


        MemberIdentifier identifer = new MemberIdentifierImpl(TestObjectWithCollection.class.getName());
        /*
        FieldMethods fieldMethods = new FieldMethods(getMethod, null);
        FieldFlags fieldFlags = new FieldFlags(null, false, null, true, "");
        GeneralControlMethods controlMethods = new GeneralControlMethods(null, null, null, null);
        MemberHelper nameHelper = new StaticHelper("list");
        DescriptiveMethods descriptiveMethods = new DescriptiveMethods(nameHelper, null);
        MemberSessionMethods sessionMethods = new MemberSessionMethods(null, null);
         */
        FacetHolder facetHolder = null;	

        Class elementType = TestPojo.class;
        listField = new JavaCollectionAssociation(identifer, elementType, facetHolder);
        
        Method getMethod = TestObjectWithCollection.class.getMethod("getList", new Class[0]);
		listField.addFacet(new JavaBeanPropertyAccessorMethod(getMethod, facetHolder));

        collection = new Object[] { "Test element" };
        testPojo = new TestObjectWithCollection(collection, false);
        testAdapter = system.createPersistentTestObject(testPojo);
    }

    public void testType() throws Exception {
        assertEquals(system.getSpecification(TestPojo.class), listField.getSpecification());
    }

    public void testGetAssociations() {
        ArrayList expectedArrayList = new ArrayList(1);
        expectedArrayList.add("Test element");

        collection[0] = "Test element";

        TestProxyNakedCollection associations = (TestProxyNakedCollection) listField.getAssociations(testAdapter);
        Vector object = (Vector) associations.getObject();
        assertEquals(1, object.size());
        assertEquals("Test element", object.elementAt(0));
    }

    public void testAdapterReflectsEmptyCollection() throws Exception {
        testPojo = new TestObjectWithCollection(new Object[0], false);
        testAdapter = system.createPersistentTestObject(testPojo);
        assertTrue(listField.isEmpty(testAdapter));
    }

    public void testAdapterReflectsNotEmptyCollection() throws Exception {
        assertFalse(listField.isEmpty(testAdapter));
    }

    public void testAddAssociation() {
        try {
            TestPojo elementToAdd = new TestPojo();
            listField.addAssociation(testAdapter, system.createPersistentTestObject(elementToAdd));
            fail();
        } catch (UnexpectedCallException expected) {}
    }

    private int size(Object[] objects) {
        int size = 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                size++;
            }
        }
        return size;
    }

    public void testRemoveAssociation() {
        try {
            TestPojo elementToAdd = new TestPojo();
            TestProxyNakedObject elementAdapter = system.createPersistentTestObject(elementToAdd);
            listField.removeAssociation(testAdapter, elementAdapter);
            fail();
        } catch (UnexpectedCallException expected) {}
    }

    public void testInitAssociations() {
        collection = new Object[2];
        testPojo = new TestObjectWithCollection(collection, false);
        testAdapter = system.createPersistentTestObject(testPojo);
        
        TestPojo elementToAdd1 = new TestPojo();
        TestPojo elementToAdd2 = new TestPojo();
        listField.initOneToManyAssociation(testAdapter, new NakedObject[] {
                system.createPersistentTestObject(elementToAdd1), system.createPersistentTestObject(elementToAdd2) });
        assertEquals(2, size(collection));
        assertEquals(elementToAdd1, collection[0]);
        assertEquals(elementToAdd2, collection[1]);
        assertEquals("1", testAdapter.getVersion().sequence());
    }

    public void testRemoveAllAssociations() {
        try {
            listField.removeAllAssociations(testAdapter);
            fail();
        } catch (UnexpectedCallException expected) {}
    }
}

// Copyright (c) Naked Objects Group Ltd.
