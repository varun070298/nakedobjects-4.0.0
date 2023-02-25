package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HibernateOidCopyFrom {
    

    @Test
    public void isEquals() {
        final HibernateOid one = HibernateOid.createPersistent(HibernateOidCopyFrom.class, "one");
        final HibernateOid copy = HibernateOid.createTransient(Object.class, 2L);

        assertFalse(one.equals(copy));
        copy.copyFrom(one);
        assertEquals(one, copy);
    }

    @Test
    public void syncsTheHibernateId() {
        final HibernateOid one = HibernateOid.createPersistent(HibernateOidCopyFrom.class, "one");
        final HibernateOid copy = HibernateOid.createTransient(Object.class, 2L);

        copy.copyFrom(one);
        assertEquals(one.getHibernateId(), copy.getHibernateId());
    }


    @Test
    public void transientStateIsCopiedOver() {
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        final HibernateOid oidCopy = HibernateOid.createPersistent(Object.class, "x");

        assertNull(oid.getHibernateId());
        assertTrue(oid.isTransient());

        assertNotNull(oidCopy.getHibernateId());
        assertFalse(oidCopy.isTransient());

        oidCopy.copyFrom(oid);
        
        assertNull(oidCopy.getHibernateId());
        assertTrue(oidCopy.isTransient());
    }

    @Test
    public void persistentStateIsCopiedOver() {
        final HibernateOid oid = HibernateOid.createPersistent(Object.class, "x");
        final HibernateOid oidCopy = HibernateOid.createTransient(Object.class, 2L);

        assertNotNull(oid.getHibernateId());
        assertFalse(oid.isTransient());

        assertNull(oidCopy.getHibernateId());
        assertTrue(oidCopy.isTransient());

        oidCopy.copyFrom(oid);
        
        assertNotNull(oidCopy.getHibernateId());
        assertFalse(oidCopy.isTransient());
    }


    @Test
    public void previousStateIsCopiedOver() {
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        final HibernateOid oidCopy = HibernateOid.createPersistent(Object.class, "x");

        oidCopy.copyFrom(oid);
        assertFalse(oid.hasPrevious());
    }

}
// Copyright (c) Naked Objects Group Ltd.
