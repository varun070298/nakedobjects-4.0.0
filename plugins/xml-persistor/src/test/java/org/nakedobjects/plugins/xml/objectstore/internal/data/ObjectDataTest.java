package org.nakedobjects.plugins.xml.objectstore.internal.data;

import java.util.Iterator;

import junit.framework.TestCase;

import org.nakedobjects.plugins.xml.objectstore.internal.clock.TestClock;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testsystem.TestSpecification;


public class ObjectDataTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ObjectDataTest.class);
    }

    public void testValueField() {
        FileVersion.setClock(new TestClock());

        final TestSpecification type = new TestSpecification();
        final ObjectData objectData = new ObjectData(type, SerialOid.createPersistent(13), new FileVersion(""));

        assertEquals(null, objectData.get("name"));
        objectData.set("name", "value");
        assertEquals("value", objectData.get("name"));

        final Iterator<String> e = objectData.fields().iterator();
        e.next();
        assertFalse(e.hasNext());

    }

}
// Copyright (c) Naked Objects Group Ltd.
