package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

public class HibernateOidCreate {

    private final Serializable primaryKey = "one";
    private final long id = 2L;
    
    @Test
    public void createPersistentUsingSerializablePrimaryKeyIsSaved() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, primaryKey);
        
        assertEquals(primaryKey, oid.getPrimaryKey());
    }

    @Test
    public void createTransientUsingSerializablePrimaryKeyIsSaved() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, primaryKey);
        
        assertEquals(primaryKey, oid.getPrimaryKey());
    }

    @Test
    public void createPersistentStoresClassName() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, primaryKey);
        
        assertEquals(HibernateOidCreate.class.getName(), oid.getClassName());
    }

    @Test
    public void createTransientStoresClassName() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, primaryKey);
        
        assertEquals(HibernateOidCreate.class.getName(), oid.getClassName());
    }

    @Test
    public void createPersistentUsesSerializablePrimaryKeyAsTheHibernateId() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, primaryKey);
        
        assertEquals(primaryKey, oid.getHibernateId());
    }

    @Test
    public void createTransientHasANullHibernateId() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, primaryKey);
        
        assertNull(oid.getHibernateId());
    }

    @Test
    public void createPersistentHasNoPrevious() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, primaryKey);
        
        assertFalse(oid.hasPrevious());
    }

    @Test
    public void createTransientHasNoPrevious() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, primaryKey);

        assertFalse(oid.hasPrevious());
    }

    
    @Test
    public void createPersistentIsNotTransient() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, primaryKey);
        
        assertFalse(oid.isTransient());
    }

    @Test
    public void createTransientIsTransient() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, primaryKey);
        
        assertTrue(oid.isTransient());
    }


    @Test
    public void createPersistentUsingLongIdIsConvertedImplicitlyAndUsedDirectlyAsSerializablePrimaryKey() {
        final HibernateOid oid = HibernateOid.createPersistent(HibernateOidCreate.class, id);
        
        Serializable primaryKey = oid.getPrimaryKey();
        assertThat(primaryKey, is(Long.class));
        Long primaryKeyAsLong = (Long) primaryKey;
        assertThat(primaryKeyAsLong.longValue(), is(id));
    }

    @Test
    public void createTransientUsingLongIdIsConvertedWithOffsetToLongAndUsedAsSerializablePrimaryKey() {
        final HibernateOid oid = HibernateOid.createTransient(HibernateOidCreate.class, id);
        
        Serializable primaryKey = oid.getPrimaryKey();
        assertThat(primaryKey, is(Long.class));
        Long primaryKeyAsLong = (Long) primaryKey;
        assertThat(primaryKeyAsLong.longValue(), is(id + HibernateOid.STANDARD_OFFSET));
    }


    
}
// Copyright (c) Naked Objects Group Ltd.
