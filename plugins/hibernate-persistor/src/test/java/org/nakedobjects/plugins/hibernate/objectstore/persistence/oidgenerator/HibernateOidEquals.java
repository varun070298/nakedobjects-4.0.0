package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class HibernateOidEquals {

    @Test
    public void testEqualsObject() {
        final HibernateOid one = HibernateOid.createPersistent(HibernateOidEquals.class, "one");
        final HibernateOid two = HibernateOid.createPersistent(HibernateOidEquals.class, "two");
        final HibernateOid oneAgain = HibernateOid.createPersistent(HibernateOidEquals.class, "one");
        final HibernateOid oneDifferentClass = HibernateOid.createPersistent(Object.class, "one");

        assertEquals(one, oneAgain);
        assertFalse(one.equals(two));
        assertFalse(one.equals(oneDifferentClass));
    }

}
// Copyright (c) Naked Objects Group Ltd.
