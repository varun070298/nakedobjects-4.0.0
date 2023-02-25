package org.nakedobjects.runtime.persistence.objectstore.transaction;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStoreTransactionManagement;
import org.nakedobjects.runtime.session.NakedObjectSession;


@RunWith(JMock.class)
public abstract class ObjectStoreTransactionManagerAbstractTestCase {

    protected Mockery mockery = new JUnit4Mockery();


    protected ObjectStoreTransactionManager transactionManager;
    protected NakedObjectSession mockSession;
    protected PersistenceSession mockPersistenceSession;
    protected ObjectStoreTransactionManagement mockObjectStore;
    
    @Before
    public void setUp() throws Exception {
        mockSession = mockery.mock(NakedObjectSession.class);
        mockPersistenceSession = mockery.mock(PersistenceSession.class);
        mockObjectStore = mockery.mock(ObjectStoreTransactionManagement.class);
    }


    
    ////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////
    

    protected void ignoreCallsToPersistenceSession() {
        mockery.checking(new Expectations() {
            {
                ignoring(mockPersistenceSession);
            }
        });
    }

    protected void ignoreCallsToObjectStore() {
        mockery.checking(new Expectations() {
            {
                ignoring(mockObjectStore);
            }
        });
    }

}
// Copyright (c) Naked Objects Group Ltd.
