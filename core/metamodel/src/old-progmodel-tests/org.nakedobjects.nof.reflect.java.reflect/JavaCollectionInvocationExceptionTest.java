package org.nakedobjects.progmodel.java5.reflect;

import java.lang.reflect.Method;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.progmodel.java5.facets.propcoll.read.JavaBeanPropertyAccessorMethod;
import org.nakedobjects.progmodel.java5.reflect.collections.JavaCollectionAssociation;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.reflect.peer.ReflectionException;
import org.nakedobjects.nof.testsystem.TestPojo;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;
import org.nakedobjects.nof.testsystem.TestProxySystem;


public class JavaCollectionInvocationExceptionTest extends TestCase {

    private TestProxySystem system;
    private JavaCollectionAssociation collectionField;
    private NakedObject testAdapter;

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        system = new TestProxySystem();
        system.init();

        MemberIdentifier identifer = new MemberIdentifierImpl(TestObjectWithCollection.class.getName());

        Class elementType = TestPojo.class;
        collectionField = new JavaCollectionAssociation(identifer, elementType);

        Method getMethod = TestObjectWithCollection.class.getMethod("getList", new Class[0]);
        collectionField.addFacet(new JavaBeanPropertyAccessorMethod(getMethod, collectionField));

        Vector collection = new Vector();
        TestObjectWithCollection testPojo = new TestObjectWithCollection(collection, true);
        testAdapter = system.createPersistentTestObject(testPojo);
    }

    public void testGetAssociations() throws Exception {
        try {
            collectionField.getAssociations(testAdapter);
            fail();
        } catch (ReflectionException e) {}
    }

    public void testAdapterReflectsEmptyCollection() throws Exception {
        try {
            collectionField.isEmpty(testAdapter);
            fail();
        } catch (ReflectionException e) {}
    }

    public void testAddAssociation() {
        try {
            TestPojo elementToAdd = new TestPojo();
            collectionField.addAssociation(testAdapter, system.createPersistentTestObject(elementToAdd));
            fail();
        } catch (ReflectionException e) {}

    }

    public void testRemoveAssociation() {
        try {
            TestPojo elementToAdd = new TestPojo();
            TestProxyNakedObject elementAdapter = system.createPersistentTestObject(elementToAdd);
            collectionField.removeAssociation(testAdapter, elementAdapter);
            fail();
        } catch (ReflectionException e) {}

    }

    public void testInitAssociations() {
        try {
            TestPojo elementToAdd1 = new TestPojo();
            TestPojo elementToAdd2 = new TestPojo();
            collectionField.initOneToManyAssociation(testAdapter, new NakedObject[] {
                    system.createPersistentTestObject(elementToAdd1), system.createPersistentTestObject(elementToAdd2) });
            fail();
        } catch (ReflectionException e) {}
    }

    public void testRemoveAllAssociations() {
        try {
            collectionField.removeAllAssociations(testAdapter);
            fail();
        } catch (ReflectionException e) {}

    }

    public void testIsAddValid() throws Exception {
        try {
            TestPojo elementToAdd = new TestPojo();
            collectionField.isAddValid(testAdapter, system.createPersistentTestObject(elementToAdd));
            fail();
        } catch (ReflectionException e) {}

    }

    public void testIsRemoveValid() throws Exception {
        try {
            TestPojo elementToAdd = new TestPojo();
            collectionField.isRemoveValid(testAdapter, system.createPersistentTestObject(elementToAdd));
            fail();
        } catch (ReflectionException e) {}
    }

}

// Copyright (c) Naked Objects Group Ltd.
