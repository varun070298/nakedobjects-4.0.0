package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import java.util.Iterator;

import org.nakedobjects.plugins.hibernate.objectstore.testdomain.BiDirectional;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.ManyToMany;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.OneToMany;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.OneToOne;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleSubClass;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;


public class PersistentNakedClassesTest extends ProxyJunit3TestCase {

    public void testNEED_TO_REINSTATE() {

    }

    public void xtestMapClasses() {
        // system.addSpecification(BiDirectional.class.getName());
        final PersistentNakedClasses classes = PersistentNakedClasses.buildPersistentNakedClasses(Boolean.TRUE);

        int count = 0;
        for (final Iterator<?> iter = classes.getPersistentClasses(); iter.hasNext();) {
            iter.next();
            count++;
        }
        assertTrue(classes.isPersistentClass(BiDirectional.class.getName()));
        assertTrue(classes.isPersistentClass(ManyToMany.class.getName()));
        assertTrue(classes.isPersistentClass(OneToMany.class.getName()));
        assertTrue(classes.isPersistentClass(OneToOne.class.getName()));
        assertEquals("classes count", 4, count);

        final PersistentNakedClass bidirPc = classes.getPersistentClass(BiDirectional.class.getName());
        assertTrue("!assn secondOneToMany", !bidirPc.hasAssociation("secondonetomany"));
        assertTrue("assn oneToMany", bidirPc.hasAssociation("oneToMany"));
        assertTrue("assn manyToMany", bidirPc.hasAssociation("manyToMany"));
        assertTrue("assn oneToOne", bidirPc.hasAssociation("oneToOne"));

        final Association assnManyToMany = bidirPc.getAssociation("manyToMany");
        assertTrue("!many to many inverse", !assnManyToMany.isInverse());
        assertEquals("many to many type", ManyToMany.class.getName(), assnManyToMany.getPersistentClass().getName());

        final PersistentNakedClass m2mPc = classes.getPersistentClass(ManyToMany.class.getName());
        assertTrue("assn many", m2mPc.hasAssociation("many"));
        final Association assnMany = m2mPc.getAssociation("many");
        assertTrue("many inverse", assnMany.isInverse());
        assertEquals("many type", BiDirectional.class.getName(), assnMany.getPersistentClass().getName());

    }

    public void xtestMapClassesNotBidirectional() {
        // system.addSpecification(BiDirectional.class.getName());
        final PersistentNakedClasses classes = PersistentNakedClasses.buildPersistentNakedClasses(Boolean.FALSE);
        final PersistentNakedClass pc = classes.getPersistentClass(BiDirectional.class.getName());
        assertTrue("!assn secondOneToMany", !pc.hasAssociation("secondonetomany"));
        assertTrue("assn oneToMany", pc.hasAssociation("oneToMany"));
        assertTrue("!assn manyToMany", !pc.hasAssociation("manytomany"));
        assertTrue("!assn oneToOne", !pc.hasAssociation("onetoone"));
    }

    public void xtestMapSubClasses() {
        // system.addSpecification(SimpleSubClass.class.getName());
        final PersistentNakedClasses classes = PersistentNakedClasses.buildPersistentNakedClasses(Boolean.TRUE);

        int count = 0;
        for (final Iterator<?> iter = classes.getPersistentClasses(); iter.hasNext();) {
            iter.next();
            count++;
        }
        assertEquals("classes count", 2, count);
        assertTrue(classes.isPersistentClass(SimpleSubClass.class.getName()));
        assertTrue(classes.isPersistentClass(SimpleObject.class.getName()));

        final PersistentNakedClass pc = classes.getPersistentClass(SimpleObject.class.getName());
        final PersistentNakedClass[] subclasses = pc.getSubClassesArray();
        assertEquals("subclasses size", 1, subclasses.length);
        assertEquals("subclass name", SimpleSubClass.class.getName(), subclasses[0].getName());
    }
}
// Copyright (c) Naked Objects Group Ltd.
