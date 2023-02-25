package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertEquals;

import org.hibernate.property.Setter;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;


public class ValueTypePropertyAccessorSetterSet {

    private SimpleObject obj = new SimpleObject();
    private String expected = "myvalue";

    @Ignore("need to convert, was originally written for the old value holder design (TextString, etc)")
    @Test
    public void testSetter() {

        NakedPropertyAccessor accessor = new NakedPropertyAccessor();
        Setter setter = accessor.getSetter(SimpleObject.class, "string");

        setter.set(obj, expected, null);
        assertEquals("string", expected, obj.getString());
    }


}
// Copyright (c) Naked Objects Group Ltd.
