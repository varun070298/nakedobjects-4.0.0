package org.nakedobjects.remoting.client.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.FindInstancesResponse;
import org.nakedobjects.remoting.exchange.GetObjectRequest;
import org.nakedobjects.remoting.exchange.GetObjectResponse;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.HasInstancesResponse;
import org.nakedobjects.remoting.exchange.OidForServiceRequest;
import org.nakedobjects.remoting.exchange.OidForServiceResponse;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;
import org.nakedobjects.remoting.exchange.ResolveFieldResponse;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionAbstract;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectstore.PersistenceSessionObjectStore;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;
import org.nakedobjects.runtime.transaction.TransactionalClosureWithReturnAbstract;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


public class PersistenceSessionProxy extends PersistenceSessionAbstract {

    final static Logger LOG = Logger.getLogger(PersistenceSessionProxy.class);

    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoderDecoder;

    /**
     * Keyed on an adapter wrapping a <tt>java.util.List</tt> (or equiv), ie with a {@link CollectionFacet}.
     */
    private final Map<NakedObjectSpecification, NakedObject> cache = new HashMap<NakedObjectSpecification, NakedObject>();
    private HashMap<String, Oid> serviceOidByNameCache = new HashMap<String, Oid>();

    
    //////////////////////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////////////////////

    public PersistenceSessionProxy(
            final PersistenceSessionFactory persistenceSessionFactory,
            final AdapterFactory adapterFactory, 
            final ObjectFactory objectFactory, 
            final ServicesInjector containerInjector, 
            final OidGenerator oidGenerator, 
            final AdapterManagerExtended adapterManager, 
            final ServerFacade serverFacade, 
            final ObjectEncoderDecoder encoder) {
        super(persistenceSessionFactory, adapterFactory, objectFactory, containerInjector, oidGenerator, adapterManager);
        this.serverFacade = serverFacade;
        this.encoderDecoder = encoder;
    }


    //////////////////////////////////////////////////////////////////
    // init, shutdown, reset, isInitialized
    //////////////////////////////////////////////////////////////////

    /**
     * TODO: mismatch between {@link SessionScopedComponent} (open) and
     * {@link ApplicationScopedComponent} (init).
     */
    @Override
    public void doOpen() {
        serverFacade.init();
    }

    /**
     * TODO: mismatch between {@link SessionScopedComponent} (open) and
     * {@link ApplicationScopedComponent} (init).
     */
    public void doClose() {
        serverFacade.shutdown();
    }


    /**
     * No need to install fixtures, rely on server-side to do the right thing. 
     */
    public boolean isFixturesInstalled() {
        return true;
    }


    //////////////////////////////////////////////////////////////////
    // loadObject, reload
    //////////////////////////////////////////////////////////////////

    public synchronized NakedObject loadObject(
    		final Oid oid, final NakedObjectSpecification hintSpec) {
        final NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        if (adapter != null) {
            return adapter;
        }
        return loadObjectFromPersistenceLayer(oid, hintSpec);
    }


	private NakedObject loadObjectFromPersistenceLayer(final Oid oid,
			final NakedObjectSpecification specHint) {
		return getTransactionManager().executeWithinTransaction(
        		new TransactionalClosureWithReturnAbstract<NakedObject>(){
			public NakedObject execute() {
				// NB: I think that the auth session must be null because we may not yet have logged in if retrieving the services.
				GetObjectRequest request = new GetObjectRequest(null, oid, specHint.getFullName());
				GetObjectResponse response = serverFacade.getObject(request);
				final ObjectData data = response.getObjectData();
				return encoderDecoder.decode(data);
			}});
	}

    public void reload(final NakedObject object) {
        final IdentityData identityData = encoderDecoder.encodeIdentityData(object);
        reloadFromPersistenceLayer(identityData);
    }


	private void reloadFromPersistenceLayer(final IdentityData identityData) {
		getTransactionManager().executeWithinTransaction(
    		new TransactionalClosureAbstract() {
				public void execute() {
					ResolveObjectRequest request = new ResolveObjectRequest(getAuthenticationSession(), identityData);
					ResolveObjectResponse response = serverFacade.resolveImmediately(request);
					final ObjectData update = response.getObjectData();
					encoderDecoder.decode(update);
				}});
	}

    
    //////////////////////////////////////////////////////////////////
    // resolveImmediately, resolveField
    //////////////////////////////////////////////////////////////////

    public synchronized void resolveImmediately(final NakedObject adapter) {
    	final ResolveState resolveState = adapter.getResolveState();
    	if (!resolveState.canChangeTo(ResolveState.RESOLVING)) {
    		return;
    	}
    	final Oid oid = adapter.getOid();
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("resolve object (remotely from server)" + oid);
    	}
    	
    	resolveImmediatelyFromPersistenceLayer(adapter);
    }

	private void resolveImmediatelyFromPersistenceLayer(final NakedObject adapter) {
		final IdentityData adapterData = encoderDecoder.encodeIdentityData(adapter);
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			public void execute() {
				ResolveObjectRequest request = new ResolveObjectRequest(
						getAuthenticationSession(), adapterData);
				// unlike the server-side implementation we don't invoke the callbacks 
				// for loading and loaded (they will already have been called in the server)
				ResolveObjectResponse response = serverFacade.resolveImmediately(
						request);
				final ObjectData data = response.getObjectData();
				@SuppressWarnings("unused")
				NakedObject decodedAdapter = encoderDecoder.decode(data);
			}
			});
	}

    public void resolveField(final NakedObject adapter, final NakedObjectAssociation field) {
        if (field.getSpecification().isCollectionOrIsAggregated()) {
            return;
        }
        final NakedObject referenceAdapter = field.get(adapter);
        if (referenceAdapter != null && referenceAdapter.getResolveState().isResolved()) {
            return;
        }
        if (referenceAdapter == null || !referenceAdapter.isPersistent()) {
            return;
        }
        if (LOG.isInfoEnabled()) {
        	LOG.info("resolveField on server: " + adapter + "/" + field.getId());
        }
        resolveFieldFromPersistenceLayer(adapter, field);
    }


	private void resolveFieldFromPersistenceLayer(final NakedObject adapter,
			final NakedObjectAssociation field) {
		final IdentityData adapterData = encoderDecoder.encodeIdentityData(adapter);
		final String fieldId = field.getId();
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract() {
			public void execute() {
				ResolveFieldRequest request = new ResolveFieldRequest(getAuthenticationSession(), adapterData, fieldId);
				ResolveFieldResponse response = serverFacade.resolveField(
						request);
				final Data data = response.getData();
				@SuppressWarnings("unused")
				NakedObject decodedAdapter = encoderDecoder.decode(data);
			}});
	}


    //////////////////////////////////////////////////////////////////
    // makePersistent
    //////////////////////////////////////////////////////////////////
    
    /**
     * REVIEW: we should perhaps have a little more symmetry here, and
     * have the {@link ServerFacade} callback to the {@link PersistenceSession}
     * (the <tt>PersistenceSessionPersist</tt> API) to handle remapping
     * of adapters.
     */
    public synchronized void makePersistent(final NakedObject object) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("makePersistent " + object);
        }
        // TODO: the PSOS has more checks; should we do the same?
		makePersistentInPersistenceLayer(object);
    }

	protected void makePersistentInPersistenceLayer(final NakedObject object) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			public void execute() {
				
		        // the two implementations (proxy vs object store) vary here.
		        // the object store does not make this call directly, it
		        // instead delegates to the PersistAlgorithm that makes a
		        // callback to the PersistenceSessionPersist API,
		        // which in turn calls remaps the adapters.
		        // 
		        // the proxy persistor on the other hand does nothing here.
		        // instead we remap the adapter in distribution code,
		        // processing the handling of the returned results.
		        //
		        // (see REVIEW comment above)

				getTransactionManager().addMakePersistent(object);
			}});
	}


    //////////////////////////////////////////////////////////////////
    // objectChanged
    //////////////////////////////////////////////////////////////////

    public void objectChanged(final NakedObject adapter) {
        if (adapter.isTransient()) {
            addObjectChangedToPresentationLayer(adapter);
            return;
        }
        if (adapter.getResolveState().respondToChangesInPersistentObjects()) {
			if (isImmutable(adapter)) {
				return;
			}
        	addObjectChangedToPersistenceLayer(adapter);
		}
    }

    private void addObjectChangedToPresentationLayer(final NakedObject adapter) {
		getUpdateNotifier().addChangedObject(adapter);
	}

	private void addObjectChangedToPersistenceLayer(final NakedObject adapter) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			public void execute() {
				getTransactionManager().addObjectChanged(adapter);
			}});
	}


    //////////////////////////////////////////////////////////////////
    // destroy
    //////////////////////////////////////////////////////////////////

    public synchronized void destroyObject(final NakedObject object) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("destroyObject " + object);
        }
        destroyObjectInPersistenceLayer(object);

        // TODO need to do garbage collection instead
        // NakedObjects.getObjectLoader().unloaded(object);
    }


	protected void destroyObjectInPersistenceLayer(final NakedObject object) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			public void execute() {
				getTransactionManager().addDestroyObject(object);
			}
		});
	}

    //////////////////////////////////////////////////////////////////
    // getInstances
    //////////////////////////////////////////////////////////////////

    @Override
    protected NakedObject[] getInstances(final PersistenceQuery persistenceQuery) {
        final NakedObjectSpecification noSpec = persistenceQuery.getSpecification();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("getInstances of " + noSpec + " with " + persistenceQuery);
        }
        
        if (canSatisfyFromCache(persistenceQuery)) {
        	return getInstancesFromCache(persistenceQuery);
        }
        
        return findInstancesFromPersistenceLayer(persistenceQuery);
    }

	private boolean canSatisfyFromCache(final PersistenceQuery persistenceQuery) {
		final NakedObjectSpecification noSpec = persistenceQuery.getSpecification();
		return cache.containsKey(noSpec) && 
		       persistenceQuery instanceof PersistenceQueryBuiltIn;
	}

	/**
	 * TODO: this code is not currently in use because there is no way to
	 * set up the cache.  We may want to change what the cache is keyed on.
	 */
	private NakedObject[] getInstancesFromCache(
			PersistenceQuery persistenceQuery) {
		final NakedObjectSpecification noSpec = persistenceQuery.getSpecification();
        PersistenceQueryBuiltIn builtIn = (PersistenceQueryBuiltIn) persistenceQuery;
		final NakedObject collection = cache.get(noSpec);
        if (!collection.getSpecification().isCollection()) {
        	return new NakedObject[0];
        }
        
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
		final List<NakedObject> instances = new ArrayList<NakedObject>();
		for (NakedObject instance : facet.iterable(collection)) {
		    if (builtIn.matches(instance)) {
		        instances.add(instance);
		    }
		}
		return (NakedObject[]) instances.toArray(new NakedObject[instances.size()]);
	}


	private NakedObject[] findInstancesFromPersistenceLayer(
			final PersistenceQuery persistenceQuery) {
		final PersistenceQueryData criteriaData = encoderDecoder.encodePersistenceQuery(persistenceQuery);
        return getTransactionManager().executeWithinTransaction(
    		new TransactionalClosureWithReturnAbstract<NakedObject[]>(){
				public NakedObject[] execute() {
					FindInstancesRequest request = new FindInstancesRequest(getAuthenticationSession(), criteriaData);
					FindInstancesResponse response = serverFacade.findInstances(request);
					final ObjectData[] instancesAsObjectData = response.getInstances();
					final NakedObject[] instances = new NakedObject[instancesAsObjectData.length];
					for (int i = 0; i < instancesAsObjectData.length; i++) {
						instances[i] = encoderDecoder.decode(instancesAsObjectData[i]);
					}
					return instances;
				}
				@Override
				public void onSuccess() {
					clearAllDirty();
				}
				});
	}


    //////////////////////////////////////////////////////////////////
    // hasInstances
    //////////////////////////////////////////////////////////////////

    public boolean hasInstances(final NakedObjectSpecification specification) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("hasInstances of " + specification);
        }
        if (cache.containsKey(specification)) {
            return hasInstancesFromCache(specification);
        }
        return hasInstancesFromPersistenceLayer(specification);
    }


	private boolean hasInstancesFromCache(
			final NakedObjectSpecification specification) {
		final NakedObject collection = (NakedObject) cache.get(specification);
		final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
		if (facet == null) {
			return false;
		} 
		return facet.size(collection) > 0;
	}


	private boolean hasInstancesFromPersistenceLayer(
			final NakedObjectSpecification specification) {
		final String specFullName = specification.getFullName();
		return getTransactionManager().executeWithinTransaction(
    		new TransactionalClosureWithReturnAbstract<Boolean>(){
				public Boolean execute() {
					HasInstancesRequest request = new HasInstancesRequest(getAuthenticationSession(), specFullName);
					HasInstancesResponse response = serverFacade.hasInstances(request);
					return response.hasInstances();
				}});
	}


    //////////////////////////////////////////////////////////////////
    // Services
    //////////////////////////////////////////////////////////////////

	/**
	 * TODO: not symmetric with {@link PersistenceSessionObjectStore}; we have 
	 * a cache but it does not? 
	 */
    @Override
    public Oid getOidForService(final String name) {
        Oid oid = serviceOidByNameCache.get(name);
        if (oid == null) {
            oid = getOidForServiceFromPersistenceLayer(name);
            registerService(name, oid);
        }
        return oid;
    }

	private Oid getOidForServiceFromPersistenceLayer(final String serviceId) {
		OidForServiceRequest request = new OidForServiceRequest(getAuthenticationSession(), serviceId);
		OidForServiceResponse response = serverFacade.oidForService(request);
		final IdentityData data = response.getOidData();
		return data.getOid();
	}

	/**
	 * TODO: not symmetric with {@link PersistenceSessionObjectStore}; we have 
	 * a cache but it does not? 
	 */
    @Override
    public void registerService(final String name, final Oid oid) {
        serviceOidByNameCache.put(name, oid);
    }


	// ///////////////////////////////////////////////////////////////////////////
	// TransactionManager
	// ///////////////////////////////////////////////////////////////////////////

    /**
     * Just downcasts.
     */
    public ClientSideTransactionManager getTransactionManager() {
        return (ClientSideTransactionManager) super.getTransactionManager();
    }


    //////////////////////////////////////////////////////////////////
    // Debugging
    //////////////////////////////////////////////////////////////////

    @Override
    public void debugData(final DebugString debug) {
        super.debugData(debug);
        debug.appendln("Server Facade", serverFacade);
    }

    public String debugTitle() {
        return "Proxy Persistence Sessino";
    }


    //////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    //////////////////////////////////////////////////////////////////
    
    private static AuthenticationSession getAuthenticationSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }

    private static UpdateNotifier getUpdateNotifier() {
        return NakedObjectsContext.getUpdateNotifier();
    }



}
// Copyright (c) Naked Objects Group Ltd.
