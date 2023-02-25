package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

public class HibernateOidMakePersistent {

    private final Serializable primaryKey = "one";
    private final long id = 2L;
    

    @Test
    public void noLongerTransientPersistent() {
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        
        assertTrue(oid.isTransient());

        oid.setHibernateId("one");
        oid.makePersistent();
        
        assertFalse(oid.isTransient());
    }

    @Test
    public void getPreviousPopulatedAndIsEqualToCopy() {
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        final HibernateOid oidCopy = HibernateOid.createPersistent(Object.class, "x");
        oidCopy.copyFrom(oid); // for later
        
        assertFalse(oid.hasPrevious());

        oid.setHibernateId("one");
        oid.makePersistent();
        
        assertTrue(oid.hasPrevious());
        assertEquals(oidCopy, oid.getPrevious());
    }

    @Test
    public void setHibernateIdIsStored() {
        Serializable hibernateId = "one";
        
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        
        assertNull(oid.getHibernateId());

        oid.setHibernateId(hibernateId);
        oid.makePersistent();
        
        assertEquals(hibernateId, oid.getHibernateId());
    }


    @Test
    public void equalToExpected() {
        final HibernateOid oid = HibernateOid.createTransient(Object.class, 2L);
        
        oid.setHibernateId("one");
        oid.makePersistent();
        
        final HibernateOid expectedPersistent = HibernateOid.createPersistent(Object.class, "one");
        assertEquals(expectedPersistent, oid);
    }

}
// Copyright (c) Naked Objects Group Ltd.
