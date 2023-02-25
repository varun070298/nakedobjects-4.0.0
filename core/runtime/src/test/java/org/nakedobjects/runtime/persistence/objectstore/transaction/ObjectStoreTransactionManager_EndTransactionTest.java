package org.nakedobjects.runtime.persistence.objectstore.transaction;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Before;
import org.junit.Test;


public class ObjectStoreTransactionManager_EndTransactionTest extends ObjectStoreTransactionManagerAbstractTestCase {

    
    

    @Before
    public void setUpTransactionManager() throws Exception {
        transactionManager = new ObjectStoreTransactionManager(
                mockPersistenceSession, mockObjectStore);
    }



    @Test
    public void endTransactionDecrementsTransactionLevel() throws Exception {
        // setup
        ignoreCallsToObjectStore();
        transactionManager.startTransaction();
        transactionManager.startTransaction();
        
        assertThat(transactionManager.transactionLevel, is(2));
        transactionManager.endTransaction();
        assertThat(transactionManager.transactionLevel, is(1));
    }

    @Test
    public void endTransactionCommitsTransactionWhenLevelDecrementsDownToZero() throws Exception {
        // setup
        ignoreCallsToObjectStore();
        transactionManager.startTransaction();
        
        mockery.checking(new Expectations() {
            {
                one(mockPersistenceSession).objectChangedAllDirty();
            }
        });
        assertThat(transactionManager.transactionLevel, is(1));
        transactionManager.endTransaction();
        assertThat(transactionManager.transactionLevel, is(0));
    }


    @Test
    public void startTransactionInteractsWithObjectStore() throws Exception {
        // setup
        ignoreCallsToPersistenceSession();
        
        mockery.checking(new Expectations() {
            {
                one(mockObjectStore).startTransaction();
            }
        });
        transactionManager.startTransaction();
        
    }

    @Test
    public void endTransactionInteractsWithObjectStore() throws Exception {
        // setup
        ignoreCallsToPersistenceSession();
        
        mockery.checking(new Expectations() {
            {
                final Sequence transactionOrdering = mockery.sequence("transactionOrdering");
                one(mockObjectStore).startTransaction();
                inSequence(transactionOrdering);
                
                one(mockObjectStore).endTransaction();
                inSequence(transactionOrdering);
            }
        });
        
        transactionManager.startTransaction();
        transactionManager.endTransaction();
    }


}
// Copyright (c) Naked Objects Group Ltd.
