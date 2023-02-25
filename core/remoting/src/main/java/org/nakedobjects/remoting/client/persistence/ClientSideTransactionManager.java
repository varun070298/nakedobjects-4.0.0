package org.nakedobjects.remoting.client.persistence;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.remoting.client.transaction.ClientSideTransaction;
import org.nakedobjects.remoting.client.transaction.ClientTransactionEvent;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.persistence.PersistenceSessionTransactionManagement;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerProxy;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManagerAbstract;
import org.nakedobjects.runtime.transaction.PersistenceCommand;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

public class ClientSideTransactionManager extends NakedObjectTransactionManagerAbstract<ClientSideTransaction> {

    final static Logger LOG = Logger.getLogger(ClientSideTransactionManager.class);

    private final AdapterManagerProxy adapterManager;
    private final PersistenceSessionTransactionManagement transactionManagement;
    private final ServerFacade connection;
    private final ObjectEncoderDecoder encoder;
    
    ////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////

    public ClientSideTransactionManager(
            final AdapterManagerProxy adapterManager, 
            final PersistenceSessionTransactionManagement transactionManagement, 
            final ServerFacade connection, 
            final ObjectEncoderDecoder encoder) {
        this.adapterManager = adapterManager;
        this.transactionManagement = transactionManagement;
        this.connection = connection;
        this.encoder = encoder;
    }

    

    ////////////////////////////////////////////////////////////////
    // start, addCommand, flush, end
    ////////////////////////////////////////////////////////////////
    
    public void startTransaction() {
        ensureTransactionNotInProgress();
        if (LOG.isDebugEnabled()) {
            LOG.debug("startTransaction");
        }
        
        // just in case...?
        transactionManagement.clearAllDirty();
        
        createTransaction();
    }

    
    /**
     * Overridable hook.
     * 
     * <p>
     * The provided {@link MessageBroker} and {@link UpdateNotifier} are obtained from
     * the hook methods ({@link #createMessageBroker()} and {@link #createUpdateNotifier()}).
     * 
     * @see #createMessageBroker()
     * @see #createUpdateNotifier()
     */
    protected ClientSideTransaction createTransaction(MessageBroker messageBroker, UpdateNotifier updateNotifier) {
        return new ClientSideTransaction(this, messageBroker, updateNotifier);
    }

    public void addCommand(final PersistenceCommand command) {
        // does nothing
    }

    public boolean flushTransaction() {
        return false;
    }


    public void endTransaction() {
        ensureTransactionInProgress();
        if (LOG.isDebugEnabled()) {
            LOG.debug("endTransaction");
        }

        if (getTransaction().isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("  no transaction commands to process");
            }
        } else {
            endNonEmptyTransaction();
        }

        getTransaction().commit();
    }


    private void endNonEmptyTransaction() {
        final KnownObjectsRequest knownObjects = new KnownObjectsRequest();

        final ClientTransactionEvent[] transactionEntries = getTransaction().getEntries();
        final ReferenceData[] referenceData = asData(transactionEntries, knownObjects);
        
        final int[] eventTypes = asEventTypes(transactionEntries);

        ExecuteClientActionResponse results;
        try {
            ExecuteClientActionRequest request = new ExecuteClientActionRequest(getAuthenticationSession(), referenceData, eventTypes);
			results = connection.executeClientAction(request);
        } catch (final ConcurrencyException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("concurrency conflict: " + e.getMessage());
            }
            final Oid oid = e.getSource();
            if (oid == null) {
                throw e;
            } else {
                final NakedObject failedObject = transactionManagement.reload(oid);
                throw new ConcurrencyException("Object automatically reloaded: "
                        + failedObject.getSpecification().getTitle(failedObject), e);
            }
        }

        if (results != null) {
            handleResults(transactionEntries, results);
        }
    }

    private int[] asEventTypes(
            final ClientTransactionEvent[] entries ) {
        
        final int numberOfEvents = entries.length;
        final int[] types = new int[numberOfEvents];

        for (int i = 0; i < numberOfEvents; i++) {
            types[i] = entries[i].getType();
        }
        return types;
    }

    private ReferenceData[] asData(
            final ClientTransactionEvent[] entries,
            final KnownObjectsRequest knownObjects ) {
        
        final int numberOfEvents = entries.length;
        final ReferenceData[] data = new ReferenceData[numberOfEvents];

        for (int i = 0; i < numberOfEvents; i++) {
            switch (entries[i].getType()) {
            case ClientTransactionEvent.ADD:
                data[i] = encoder.encodeMakePersistentGraph(entries[i].getObject(), knownObjects);
                break;
            case ClientTransactionEvent.CHANGE:
                data[i] = encoder.encodeGraphForChangedObject(entries[i].getObject(), knownObjects);
                break;
            case ClientTransactionEvent.DELETE:
                data[i] = encoder.encodeIdentityData(entries[i].getObject());
                break;
            }
        }
        return data;
    }

    
    private void handleResults(
            final ClientTransactionEvent[] entries,
            final ExecuteClientActionResponse results) {
        
        final int numberOfEvents = entries.length;

        final int[] eventTypes = asEventTypes(entries);

        final ReferenceData[] persistedUpdates = results.getPersisted();
        final Version[] changedVersions = results.getChanged();
        
        for (int i = 0; i < numberOfEvents; i++) {
            switch (eventTypes[i]) {
                case ClientTransactionEvent.ADD:
                    // causes OID to be updated
                    final ReferenceData update = persistedUpdates[i];
                    
                    Oid updatedOid = update.getOid();
                    adapterManager.remapUpdated(updatedOid);
                    final NakedObject adapter = adapterManager.getAdapterFor(updatedOid);
                    
                    adapter.changeState(ResolveState.RESOLVED);
                    entries[i].getObject().setOptimisticLock(update.getVersion());
    
                    break;
                case ClientTransactionEvent.CHANGE:
                    entries[i].getObject().setOptimisticLock(changedVersions[i]);
                    getUpdateNotifier().addChangedObject(entries[i].getObject());
                    break;
                }
        }

        final ObjectData[] updates = results.getUpdates();
        for (int i = 0; i < updates.length; i++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("update " + updates[i].getOid());
            }
            encoder.decode(updates[i]);
        }

        for (int i = 0; i < numberOfEvents; i++) {
            switch (eventTypes[i]) {
            case ClientTransactionEvent.DELETE:
                getUpdateNotifier().addDisposedObject(entries[i].getObject());
                break;
            }
        }
    }


    public void abortTransaction() {
        ensureTransactionInProgress();
        if (LOG.isDebugEnabled()) {
            LOG.debug("abortTransaction");
        }
        getTransaction().abort();
    }


    
    //////////////////////////////////////////////////////////////////
    // Not public API 
    //////////////////////////////////////////////////////////////////

    public void addMakePersistent(NakedObject object) {
        ensureTransactionInProgress();

        getTransaction().addMakePersistent(object);
    }

    public void addObjectChanged(NakedObject object) {
        ensureTransactionInProgress();

        getTransaction().addObjectChanged(object);
    }

    public void addDestroyObject(NakedObject object) {
        ensureTransactionInProgress();

        getTransaction().addDestroyObject(object);    
    }



    //////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    //////////////////////////////////////////////////////////////////
    
    private AuthenticationSession getAuthenticationSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }


}


// Copyright (c) Naked Objects Group Ltd.
