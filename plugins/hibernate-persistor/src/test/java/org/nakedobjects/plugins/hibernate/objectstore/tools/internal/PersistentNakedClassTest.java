package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import junit.framework.TestCase;

import org.nakedobjects.runtime.testsystem.TestSpecification;


public class PersistentNakedClassTest extends TestCase {
    private static String className = "myclass";
    private PersistentNakedClass root;
    private PersistentNakedClass pc;
    private PersistentNakedClass pc2;
    private PersistentNakedClass pc3;
    private TestSpecification spec;
    private TestSpecification spec2;
    private TestSpecification spec3;

    @Override
    public void setUp() {
        root = new PersistentNakedClass();

        spec = new TestSpecification(className);
        pc = new PersistentNakedClass(spec, root);

        spec2 = new TestSpecification(className + "2");
        pc2 = new PersistentNakedClass(spec2, root);

        spec3 = new TestSpecification(className + "3");
        pc3 = new PersistentNakedClass(spec3, pc2);
    }

    @Override
    public void tearDown() {
        root = pc = pc2 = pc3 = null;
        spec = spec2 = spec3 = null;
    }

    public void testBasic() {
        final PersistentNakedClass pcDuplicate = new PersistentNakedClass(spec, root);

        assertEquals(className, pc.getName());
        assertEquals(pc, pcDuplicate);
        assertEquals("name", className, pc.getName());
        assertFalse("pc3=pc", pc3.equals(pc));
        assertEquals("parent", pc2, pc3.getParent());

        final PersistentNakedClass[] pcSubclasses = pc2.getSubClassesArray();
        assertEquals("subclasses size", 1, pcSubclasses.length);
        assertEquals("subclasses 0", pc3, pcSubclasses[0]);
    }

    public void testRemoveFromHierarchy() {
        // validate setup
        pc2.setParent(pc);
        assertEquals(pc, pc2.getParent());
        assertEquals(pc2, pc3.getParent());

        pc2.removeFromHierarchy();

        assertNull("pc2 parent", pc2.getParent());
        assertEquals("pc3 parent", pc, pc3.getParent());
        final PersistentNakedClass[] pcSubclasses = pc.getSubClassesArray();
        assertEquals("subclasses size", 1, pcSubclasses.length);
        assertEquals("subclasses 0", pc3, pcSubclasses[0]);
    }

    public void testSetParent() {
        // validate setup
        assertEquals(pc2, pc3.getParent());
        assertEquals(0, pc.getSubClassesArray().length);

        pc3.setParent(pc);

        assertEquals("parent", pc, pc3.getParent());
        assertEquals("pc2 subclasses", 0, pc2.getSubClassesArray().length);

        final PersistentNakedClass[] pcSubclasses = pc.getSubClassesArray();
        assertEquals("subclasses size", 1, pcSubclasses.length);
        assertEquals("subclasses 0", pc3, pcSubclasses[0]);
    }

    // public void testGetUniqueFields() {
    // NakedObjectField[] fields2 = new NakedObjectField[3];
    // NakedObjectField[] fields3 = new NakedObjectField[5];
    // for (int i = 0; i < fields2.length; i++) {
    // fields2[i] = fields3[i] = new TestValueAssociation(null, "field"+i, null);
    // }
    // for (int i = fields2.length; i < fields3.length; i++) {
    // fields3[i] = new TestValueAssociation(null, "field"+i, null);
    // }
    // spec2.setupFields(fields2);
    // spec3.setupFields(fields3);
    //		
    // NakedObjectField[] unique2 = pc2.getUniqueFields();
    // assertEquals("unique2 len", 3, unique2.length);
    // assertEquals("unique2", fields2, unique2);
    // NakedObjectField[] unique3 = pc3.getUniqueFields();
    // assertEquals("unique3 len", 2, unique3.length);
    // for (int i = 0; i < unique3.length; i++) {
    // assertEquals("unique3 item "+i, fields3[i+3], unique3[i]);
    // }
    // }

    // public void testGetUniqueAssociation() {
    // NakedObjectField[] fields2 = new NakedObjectField[3];
    // fields2[0] = new TestOneToOneAssociation(null, "field1", spec);
    // fields2[1] = new TestOneToOneAssociation(null, "field2", spec);
    // fields2[2] = new TestOneToOneAssociation(null, "field3", spec3);
    // spec2.setupFields(fields2);
    //		
    // assertNull("unique assn spec", pc2.getUniqueAssociation(className));
    // NakedObjectField unique = pc2.getUniqueAssociation(className+"3");
    // assertNotNull("unique assn spec3", unique);
    // assertEquals("unique", fields2[2], unique);
    // }
}
// Copyright (c) Naked Objects Group Ltd.
