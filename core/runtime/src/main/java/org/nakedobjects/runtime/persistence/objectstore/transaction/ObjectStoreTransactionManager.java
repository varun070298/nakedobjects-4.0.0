package org.nakedobjects.runtime.persistence.objectstore.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import org.apache.log4j.Logger;
import org.nakedobjects.runtime.persistence.PersistenceSessionTransactionManagement;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStoreTransactionManagement;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManagerAbstract;
import org.nakedobjects.runtime.transaction.PersistenceCommand;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


public class ObjectStoreTransactionManager extends NakedObjectTransactionManagerAbstract<ObjectStoreTransaction> {

    private static final Logger LOG = Logger.getLogger(ObjectStoreTransactionManager.class);

    private final PersistenceSessionTransactionManagement objectPersistor;
    private final ObjectStoreTransactionManagement objectStore;
    
    
    // package level visibility so tests can look at directly
    int transactionLevel;

    public ObjectStoreTransactionManager(
            final PersistenceSessionTransactionManagement objectPersistor, 
            final ObjectStoreTransactionManagement objectStore) {
        this.objectPersistor = objectPersistor;
        this.objectStore = objectStore;
    }


    ////////////////////////////////////////////////////////
    // start, flush, abort, end
    ////////////////////////////////////////////////////////

    public void startTransaction() {
        
    	boolean noneInProgress = false;
        if (getTransaction() == null || getTransaction().getState().isComplete()) {
        	noneInProgress = true;
        	
            createTransaction();
            transactionLevel = 0;
            objectStore.startTransaction();
        }
        
        transactionLevel++;
        
        if (LOG.isInfoEnabled()) {
        	LOG.info(
        			"startTransaction: level " + 
        			(transactionLevel-1) + "->" + (transactionLevel) + 
        			(noneInProgress?" (no transaction in progress or was previously completed; transaction created)":""));
        }
    }

    public boolean flushTransaction() {
    	
        if (LOG.isDebugEnabled()) {
        	LOG.debug("flushTransaction");
        }

        if (getTransaction() != null) {
            objectPersistor.objectChangedAllDirty();
            getTransaction().flush();
        }
        return false;
    }

    public void endTransaction() {
        if (LOG.isInfoEnabled()) {
        	LOG.info("endTransaction: level " + 
        			(transactionLevel) + "->" + (transactionLevel-1));
        }

        transactionLevel--;
        if (transactionLevel == 0) {
        	LOG.info("endTransaction: committing");
            objectPersistor.objectChangedAllDirty();
            getTransaction().commit();
            objectStore.endTransaction();
        } else if (transactionLevel < 0) {
        	LOG.error("endTransaction: transactionLevel=" + transactionLevel);
            transactionLevel = 0;
            throw new IllegalStateException(" no transaction running to end (transactionLevel < 0)");
        }
    }

    public void abortTransaction() {
        if (getTransaction() != null) {
            getTransaction().abort();
            transactionLevel = 0;
            objectStore.abortTransaction();
        }
    }

    
    //////////////////////////////////////////////////////////////////
    // Not public API 
    //////////////////////////////////////////////////////////////////

    public void addCommand(final PersistenceCommand command) {
        getTransaction().addCommand(command);
    }


    
    ////////////////////////////////////////////////////////////////
    // Hooks
    ////////////////////////////////////////////////////////////////

    protected ObjectStoreTransaction createTransaction(
            final MessageBroker messageBroker, 
            final UpdateNotifier updateNotifier) {
        ensureThatArg(messageBroker, is(not(nullValue())));
        ensureThatArg(updateNotifier, is(not(nullValue())));

        return new ObjectStoreTransaction(this, messageBroker, updateNotifier, objectStore);
    }




}

// Copyright (c) Naked Objects Group Ltd.
