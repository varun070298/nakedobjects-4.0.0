package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertEquals;

import org.hibernate.property.Getter;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;


public class ValueTypePropertyAccessorGetterGet {

    private SimpleObject obj = new SimpleObject();
    private String expected = "myvalue";

    @Ignore("need to convert, was originally written for the old value holder design (TextString, etc)")
    @Test
    public void testGetter() {
        obj.setString(expected);

        NakedPropertyAccessor accessor = new NakedPropertyAccessor();
        Getter getter = accessor.getGetter(SimpleObject.class, "string");

        assertEquals("string", expected, getter.get(obj));
        assertEquals("string", expected, getter.getForInsert(obj, null, null));
    }

}
// Copyright (c) Naked Objects Group Ltd.
