package org.nakedobjects.runtime.persistence.objectstore.transaction;


import org.junit.Test;


public class ObjectStoreTransactionManager_InstantiationTest extends ObjectStoreTransactionManagerAbstractTestCase {


    @Test
    public void canInstantiate() throws Exception {
        transactionManager = new ObjectStoreTransactionManager(
                mockPersistenceSession, mockObjectStore);
    }

}
// Copyright (c) Naked Objects Group Ltd.
