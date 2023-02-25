package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.property.Setter;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.TestObject;

public class UserAccessorGetSetter extends UserAccessorAbstractTestCase {


    @Test
    public void happyCase() {
        final UserAccessor accessor = new UserAccessor();
        final Setter setter = accessor.getSetter(TestObject.class, PropertyHelper.MODIFIED_BY);
        assertNotNull(setter);
        assertNull("getMethod", setter.getMethod());
        assertNull("getMethodName", setter.getMethodName());
    }


}
// Copyright (c) Naked Objects Group Ltd.
