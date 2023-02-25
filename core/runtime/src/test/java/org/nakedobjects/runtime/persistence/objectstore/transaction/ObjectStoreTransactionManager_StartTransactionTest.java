package org.nakedobjects.runtime.persistence.objectstore.transaction;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;


public class ObjectStoreTransactionManager_StartTransactionTest extends ObjectStoreTransactionManagerAbstractTestCase {

    @Before
    public void setUpTransactionManager() throws Exception {
        transactionManager = new ObjectStoreTransactionManager(
                mockPersistenceSession, mockObjectStore);
    }

    @Before
    public void setUpExpectations() throws Exception {
        ignoreCallsToPersistenceSession();
    }



    @Test
    public void startTransactionCreateTransactionIfNone() throws Exception {
        ignoreCallsToObjectStore();
        
        assertThat(transactionManager.getTransaction(), is(nullValue()));
        transactionManager.startTransaction();
        assertThat(transactionManager.getTransaction(), is(not(nullValue())));
    }

    @Test
    public void startTransactionDoesNotOverwriteTransactionIfHasOne() throws Exception {
        ignoreCallsToObjectStore();

        // cause a transaction to be created
        transactionManager.startTransaction();
        ObjectStoreTransaction transactionAfterFirstStart = transactionManager.getTransaction();
        
        transactionManager.startTransaction();
        
        assertThat(transactionManager.getTransaction(), is(sameInstance(transactionAfterFirstStart)));
    }

    @Test
    public void startTransactionIncrementsTransactionLevel() throws Exception {
        ignoreCallsToObjectStore();

        assertThat(transactionManager.transactionLevel, is(0));
        transactionManager.startTransaction();
        assertThat(transactionManager.transactionLevel, is(1));
    }

    @Test
    public void startTransactionCallsStartTransactionOnObjectStore() throws Exception {
        mockery.checking(new Expectations() {
            {
                one(mockObjectStore).startTransaction();
            }
        });

        transactionManager.startTransaction();
    }

    
}
// Copyright (c) Naked Objects Group Ltd.
