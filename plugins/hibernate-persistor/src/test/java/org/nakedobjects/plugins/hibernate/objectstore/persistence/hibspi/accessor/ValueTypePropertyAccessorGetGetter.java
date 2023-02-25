package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.property.Getter;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;



public class ValueTypePropertyAccessorGetGetter {

    private SimpleObject obj = new SimpleObject();
    private String expected = "myvalue";


    @Ignore("need to convert, was originally written for the old value holder design (TextString, etc)")
    @Test
    public void happyCase() {
        obj.setString(expected);

        NakedPropertyAccessor accessor = new NakedPropertyAccessor();
        Getter getter = accessor.getGetter(SimpleObject.class, "string");
        assertNotNull(getter);
        assertNull("getMethod", getter.getMethod());
        assertNull("getMethodName", getter.getMethodName());
        assertEquals("return type", String.class, getter.getReturnType());
    }

}
// Copyright (c) Naked Objects Group Ltd.
