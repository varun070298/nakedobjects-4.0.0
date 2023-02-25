package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.property.Getter;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.TestObject;

public class UserAccessorGetGetter extends UserAccessorAbstractTestCase {


    @Test
    public void happyCase() {
        final UserAccessor accessor = new UserAccessor();
        final Getter getter = accessor.getGetter(TestObject.class, PropertyHelper.MODIFIED_BY);
        assertNotNull(getter);
        assertNull("getMethod", getter.getMethod());
        assertNull("getMethodName", getter.getMethodName());
    }

}
// Copyright (c) Naked Objects Group Ltd.
