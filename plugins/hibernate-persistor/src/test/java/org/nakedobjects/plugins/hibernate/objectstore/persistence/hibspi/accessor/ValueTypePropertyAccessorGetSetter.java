package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.property.Setter;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;


public class ValueTypePropertyAccessorGetSetter {

    private SimpleObject obj = new SimpleObject();
    private String expected = "myvalue";


    @Ignore("need to convert, was originally written for the old value holder design (TextString, etc)")
    @Test
    public void happyCase() {

        NakedPropertyAccessor accessor = new NakedPropertyAccessor();
        Setter setter = accessor.getSetter(SimpleObject.class, "string");
        assertNotNull(setter);
        assertNull("getMethod", setter.getMethod());
        assertNull("getMethodName", setter.getMethodName());
    }


}
// Copyright (c) Naked Objects Group Ltd.
