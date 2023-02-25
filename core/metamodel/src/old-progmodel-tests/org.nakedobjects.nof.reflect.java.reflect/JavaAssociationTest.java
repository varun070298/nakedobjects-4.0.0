package org.nakedobjects.progmodel.java5.reflect;

import java.lang.reflect.Method;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.progmodel.java5.facets.propcoll.read.JavaBeanPropertyAccessorMethod;
import org.nakedobjects.progmodel.java5.facets.properties.write.ClearPropertyViaSetterMethodFacet;
import org.nakedobjects.progmodel.java5.facets.properties.write.JavaBeanPropertySetterMethodFacet;
import org.nakedobjects.progmodel.java5.reflect.properties.JavaOneToOneAssociation;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.TestProxySystem;



public class JavaAssociationTest extends TestCase {
    private static final String PERSON_FIELD_NAME = "person";
    private JavaObjectWithOneToOneAssociations adapter;
    private NakedObject targetAdapter;
    private JavaOneToOneAssociation personField;
    private JavaReferencedObject associate;
    private NakedObject associateAdapter;
    private TestProxySystem system;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(JavaAssociationTest.class));
    }

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        system = new TestProxySystem();
        system.init();

        adapter = new JavaObjectWithOneToOneAssociations();
        targetAdapter = system.createAdapterForTransient(adapter);

        MemberIdentifierImpl identifier = new MemberIdentifierImpl("cls", PERSON_FIELD_NAME);
        personField = new JavaOneToOneAssociation(identifier, JavaReferencedObject.class);

        Class cls = JavaObjectWithOneToOneAssociations.class;
        Method get = cls.getDeclaredMethod("getReferencedObject", new Class[0]);
        personField.addFacet(new JavaBeanPropertyAccessorMethod(get, personField));
        
        Method set = cls.getDeclaredMethod("setReferencedObject", new Class[] { JavaReferencedObject.class });
        personField.addFacet(new JavaBeanPropertySetterMethodFacet(set, personField));       
        personField.addFacet(new ClearPropertyViaSetterMethodFacet(set, personField));
        

//        FieldFlags fieldFlags = new FieldFlags(When.NEVER, false, When.NEVER, false, "");

        associate = new JavaReferencedObject();
        associateAdapter = system.createAdapterForTransient(associate);
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }
    
    public void testFieldNotSet() {
        assertNull(adapter.getReferencedObject());
    }
    
    public void testGetReturnNull() throws Exception {
        assertNull(personField.getAssociation(targetAdapter));
    }
    
    public void testSetViaMethod() {
        personField.setAssociation(targetAdapter, associateAdapter);
        assertEquals(associate, adapter.getReferencedObject());
    }

    public void testClear() {
        adapter.setReferencedObject(associate);
        assertEquals(associate, adapter.getReferencedObject());

        personField.clearAssociation(targetAdapter, associateAdapter);
        assertNull(adapter.getReferencedObject());
    }

    public void testGet() {
        adapter.setReferencedObject(associate);
        assertEquals(associateAdapter, personField.getAssociation(targetAdapter));
    }

    public void testInitGet() {
        personField.initAssociation(targetAdapter, associateAdapter);
        assertEquals(associate, adapter.getReferencedObject());
    }
    
    public void test() throws Exception {
        personField.getDefault(targetAdapter);
    }
}
// Copyright (c) Naked Objects Group Ltd.
