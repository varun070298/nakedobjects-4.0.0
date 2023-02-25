package org.nakedobjects.runtime.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.applib.query.QueryDefault;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.services.container.query.QueryFindByPattern;
import org.nakedobjects.metamodel.services.container.query.QueryFindByTitle;
import org.nakedobjects.metamodel.spec.Dirtiable;
import org.nakedobjects.metamodel.spec.IntrospectableSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification.CreationMode;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByPattern;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQueryDefault;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQuerySerializable;
import org.nakedobjects.runtime.persistence.services.ServiceUtil;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


public abstract class PersistenceSessionAbstract implements PersistenceSession {
    private static final Logger LOG = Logger.getLogger(PersistenceSessionAbstract.class);

    private final PersistenceSessionFactory persistenceSessionFactory;
    private final AdapterFactory adapterFactory;
    private final ObjectFactory objectFactory;
    private final ServicesInjector servicesInjector;
    private final OidGenerator oidGenerator;
    private final AdapterManagerExtended adapterManager;

    private boolean dirtiableSupport;

    /**
     * Injected using setter-based injection.
     */
    private SpecificationLoader specificationLoader;
    /**
     * Injected using setter-based injection.
     */
    private NakedObjectTransactionManager transactionManager;

    
    private static enum State {
        NOT_INITIALIZED,
        OPEN,
        CLOSED
    }
    private State state;
    
    public PersistenceSessionAbstract(
            final PersistenceSessionFactory persistenceSessionFactory, 
            final AdapterFactory adapterFactory, 
            final ObjectFactory objectFactory, 
            final ServicesInjector servicesInjector, 
            final OidGenerator oidGenerator, 
            final AdapterManagerExtended identityMap) {

        ensureThatArg(persistenceSessionFactory, is(not(nullValue())), "persistence session factory required");
        
        ensureThatArg(adapterFactory, is(not(nullValue())), "adapter factory required");
        ensureThatArg(objectFactory, is(not(nullValue())), "object factory required");
        ensureThatArg(servicesInjector, is(not(nullValue())), "services injector required");
        ensureThatArg(oidGenerator, is(not(nullValue())), "OID generator required");
        ensureThatArg(identityMap, is(not(nullValue())), "identity map required");

        // owning, application scope
        this.persistenceSessionFactory = persistenceSessionFactory;
        
        // session scope
        this.adapterFactory = adapterFactory;
        this.objectFactory = objectFactory;
        this.servicesInjector = servicesInjector;
        this.oidGenerator = oidGenerator;
        this.adapterManager = identityMap;

        setState(State.NOT_INITIALIZED);
    }


    
    // ///////////////////////////////////////////////////////////////////////////
    // PersistenceSessionFactory
    // ///////////////////////////////////////////////////////////////////////////
    
    public PersistenceSessionFactory getPersistenceSessionFactory() {
        return persistenceSessionFactory;
    }

    
    // ///////////////////////////////////////////////////////////////////////////
    // open, close
    // ///////////////////////////////////////////////////////////////////////////


    /**
     * Injects components, calls {@link #doOpen()}, and then creates service adapters.
     * 
     * @see #doOpen()
     */
    public final void open() {
        ensureNotOpened();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("opening " + this);
        }
        
        // injected via setters
        ensureThatState(specificationLoader, is(not(nullValue())), "SpecificationLoader missing");
        ensureThatState(transactionManager, is(not(nullValue())), "TransactionManager missing");
        
        // inject any required dependencies into object factory
        this.injectInto(objectFactory);
        specificationLoader.injectInto(objectFactory);
        servicesInjector.injectInto(objectFactory);

        // wire dependencies into identityMap
        adapterFactory.injectInto(adapterManager);
        specificationLoader.injectInto(adapterManager);
        oidGenerator.injectInto(adapterManager);
        servicesInjector.injectInto(adapterManager);

        // wire dependencies into oid generator
        specificationLoader.injectInto(oidGenerator);
        
        servicesInjector.open();
        adapterFactory.open();
        objectFactory.open();
        adapterManager.open();
        oidGenerator.open();

        doOpen();
        createServiceAdapters();
        
        setState(State.OPEN);
        doOpened();
    }


    /**
     * Calls {@link #doClose()}, then closes all components.
     * 
     * @see #doClose()
     */
    public final void close() {
        if (getState() == State.CLOSED) {
            // nothing to do
            return;
        }
       
        if (LOG.isInfoEnabled()) {
            LOG.info("closing " + this);
        }

        doClose();
        
        adapterManager.close();
        servicesInjector.close();
        objectFactory.close();
        adapterFactory.close();
        oidGenerator.close();
        
        setState(State.CLOSED);
    }



    /**
     * Optional hook method called prior to creating service adapters for subclass 
     * to initialize its components.
     */
    protected void doOpen() {}

    /**
     * Optional hook method for any final processing from {@link #open()}.
     */
    protected void doOpened() {}


    /**
     * Optional hook method to close subclass' components.
     */
    protected void doClose() {}






    /**
     * Creates (or recreates following a {@link #testReset()}) {@link NakedObject adapters}
     * for the {@link #serviceList}.
     */
    private void createServiceAdapters() {
        getTransactionManager().startTransaction();
        for (final Object service : servicesInjector.getRegisteredServices()) {
            NakedObjectSpecification serviceNoSpec = specificationLoader.loadSpecification(service.getClass());
            if (serviceNoSpec instanceof IntrospectableSpecification) {
                IntrospectableSpecification introspectableSpecification = (IntrospectableSpecification) serviceNoSpec;
                introspectableSpecification.markAsService();
            }
            final String serviceId = ServiceUtil.id(service);
            final Oid existingOid = getOidForService(serviceId);
            NakedObject adapter;
            if (existingOid == null) {
                adapter = getAdapterManager().adapterFor(service);
            } else {
                adapter = getAdapterManager().recreateRootAdapter(existingOid, service);
            }
            
            if (adapter.getOid().isTransient()) {
            	adapterManager.remapAsPersistent(adapter);
            }
            
            if (adapter.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            	adapter.changeState(ResolveState.RESOLVING);
            	adapter.changeState(ResolveState.RESOLVED);
            }
            if (existingOid == null) {
                final Oid persistentOid = adapter.getOid();
                registerService(serviceId, persistentOid);
            }
            
        }
        getTransactionManager().endTransaction();
    }

    
    private State getState() {
        return state;
    }
    private void setState(State state) {
        this.state = state;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // State Management
    // ///////////////////////////////////////////////////////////////////////////

    protected void ensureNotOpened() {
        if (getState() != State.NOT_INITIALIZED) {
            throw new IllegalStateException("Persistence session has already been initialized");
        }
    }
    
    protected void ensureOpen() {
        if (getState() != State.OPEN) {
            throw new IllegalStateException("Persistence session is not open");
        }
    }
    
    // ///////////////////////////////////////////////////////////////////////////
    // shutdown, reset
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * For testing purposes only.
     */
    public void testReset() {
    }
    




    // ///////////////////////////////////////////////////////////////////////////
    // Factory (for transient instance)
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Create a root or standalone {@link NakedObject adapter}.
     * 
     * <p>
     * The returned object will be initialied (had the relevant callback lifecycle methods 
     * invoked).
     * 
     * <p>
     * TODO: this is the same as {@link RuntimeContextFromSession#createTransientInstance(NakedObjectSpecification)}; could it be unified?
     */
    public NakedObject createInstance(final NakedObjectSpecification specification) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("creating transient instance of " + specification);
    	}
        final Object pojo = specification.createObject(CreationMode.INITIALIZE);
        return getAdapterManager().adapterFor(pojo);
    }

    public NakedObject recreateAdapter(final Oid oid, final NakedObjectSpecification specification) {
        final NakedObject adapterLookedUpByOid = getAdapterManager().getAdapterFor(oid);
        if (adapterLookedUpByOid != null) {
            return adapterLookedUpByOid;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("recreating adapter for Oid: " + oid + " of type " + specification);
        }
        final Object pojo = specification.createObject(CreationMode.NO_INITIALIZE);
        
        return getAdapterManager().recreateRootAdapter(oid, pojo);
    }


    public NakedObject recreateAdapter(final Oid oid, final Object pojo) {
        final NakedObject adapterLookedUpByOid = getAdapterManager().getAdapterFor(oid);
        if (adapterLookedUpByOid != null) {
            return adapterLookedUpByOid;
        }

        final NakedObject adapterLookedUpByPojo = getAdapterManager().getAdapterFor(pojo);
        if (adapterLookedUpByPojo != null) {
            return adapterLookedUpByPojo;
        }

        if (LOG.isDebugEnabled()) {
            // don't touch pojo in case cause it to resolve.
            LOG.debug("recreating adapter for Oid: " + oid + " for provided pojo ");
        }
        return getAdapterManager().recreateRootAdapter(oid, pojo);
    }



    
    // ///////////////////////////////////////////////////////////////////////////
    // reload
    // ///////////////////////////////////////////////////////////////////////////

    public NakedObject reload(Oid oid) {
        NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        reload(adapter);
        return adapter;
    }
    

    public abstract void reload(NakedObject adapter);
    
    
    // ///////////////////////////////////////////////////////////////////////////
    // findInstances, getInstances
    // ///////////////////////////////////////////////////////////////////////////

    public <T> NakedObject findInstances(Query<T> query, QueryCardinality cardinality) {
    	final PersistenceQuery persistenceQuery = createPersistenceQueryFor(query, cardinality);
        if (persistenceQuery == null) {
            throw new IllegalArgumentException("Unknown query type: " + query.getDescription());
        }
        return findInstances(persistenceQuery);
    }

    public NakedObject findInstances(PersistenceQuery persistenceQuery) {
        final NakedObject[] instances = getInstances(persistenceQuery);
        final NakedObjectSpecification specification = persistenceQuery.getSpecification();
        final NakedObjectList results = new NakedObjectList(specification, instances);
        return getAdapterManager().adapterFor(results);
    }

    /**
     * Converts the {@link Query applib representation of a query} into the
     * {@link PersistenceQuery NOF-internal representation}. 
     */
	protected final PersistenceQuery createPersistenceQueryFor(Query<?> query, QueryCardinality cardinality) {
		LOG.info("createPersistenceQueryFor: " + query.getDescription());
		NakedObjectSpecification noSpec = specFor(query);
	    if (query instanceof QueryFindAllInstances) {
			return new PersistenceQueryFindAllInstances(noSpec);
	    }
	    if (query instanceof QueryFindByTitle) {
			QueryFindByTitle<?> queryByTitle = (QueryFindByTitle<?>) query;
	    	String title = queryByTitle.getTitle();
			return new PersistenceQueryFindByTitle(noSpec, title);
	    }
	    if (query instanceof QueryFindByPattern) {
			QueryFindByPattern<?> queryByPattern = (QueryFindByPattern<?>) query;
			Object pattern = queryByPattern.getPattern();
			NakedObject patternAdapter = getAdapterManager().adapterFor(pattern);
			return new PersistenceQueryFindByPattern(noSpec, patternAdapter);
	    }
	    if (query instanceof QueryDefault) {
			QueryDefault<?> queryDefault = (QueryDefault<?>) query;
			String queryName = queryDefault.getQueryName();
			Map<String, NakedObject> argumentsAdaptersByParameterName = wrap(queryDefault.getArgumentsByParameterName());
			return new PersistenceQueryFindUsingApplibQueryDefault(noSpec, queryName, argumentsAdaptersByParameterName, cardinality);
	    }
	    // fallback; generic serializable applib query.
	    return new PersistenceQueryFindUsingApplibQuerySerializable(noSpec, query, cardinality);
	}

	private NakedObjectSpecification specFor(Query<?> query) {
		return getSpecificationLoader().loadSpecification(query.getResultType());
	}

	/**
	 * Converts a map of pojos keyed by string to a map of adapters keyed by the
	 * same strings.
	 */
	private Map<String, NakedObject> wrap(
			Map<String, Object> argumentsByParameterName) {
		Map<String, NakedObject> argumentsAdaptersByParameterName = new HashMap<String, NakedObject>();
		for (String parameterName: argumentsByParameterName.keySet()) {
			Object argument = argumentsByParameterName.get(parameterName);
			NakedObject argumentAdapter = argument != null? getAdapterManager().adapterFor(argument): null;
			argumentsAdaptersByParameterName.put(parameterName, argumentAdapter);
		}
		return argumentsAdaptersByParameterName;
	}


	protected abstract NakedObject[] getInstances(final PersistenceQuery persistenceQuery);

    
    // ///////////////////////////////////////////////////////////////////////////
    // Manual dirtying support
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * @see #setDirtiableSupport(boolean)
     */
    public boolean isCheckObjectsForDirtyFlag() {
        return dirtiableSupport;
    }

    /**
     * Whether to notice {@link Dirtiable manually-dirtied} objects.
     */
    public void setDirtiableSupport(final boolean checkObjectsForDirtyFlag) {
        this.dirtiableSupport = checkObjectsForDirtyFlag;
    }

    /**
     * If {@link #isCheckObjectsForDirtyFlag() enabled}, will mark as {@link #objectChanged(NakedObject) changed}
     * any {@link Dirtiable} objects that have manually been {@link Dirtiable#markDirty(NakedObject) marked as dirty}. 
     */
    public void objectChangedAllDirty() {
        if (!dirtiableSupport) {
            return;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("marking as changed any objects that have been manually set as dirty");
        }
        for (final NakedObject adapter: getAdapterManager()) {
            if (adapter.getSpecification().isDirty(adapter)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("  found dirty object " + adapter);
                }
                objectChanged(adapter);
                adapter.getSpecification().clearDirty(adapter);
            }
        }
    }

    /**
     * Set as {@link Dirtiable#clearDirty(NakedObject) clean} any {@link Dirtiable} objects.
     */
    public synchronized void clearAllDirty() {
        if (!isCheckObjectsForDirtyFlag()) {
            return;
        } 
        if (LOG.isDebugEnabled()) {
            LOG.debug("cleaning any manually dirtied objects");
        }
        
        for (final NakedObject object: getAdapterManager()) {
            if (object.getSpecification().isDirty(object)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("  found dirty object " + object);
                }
                object.getSpecification().clearDirty(object);
            }
        }
        
    }

    // ///////////////////////////////////////////////////////////////////////////
    // AdaptedServiceManager
    // ///////////////////////////////////////////////////////////////////////////


    /**
     * Returns the OID for the adapted service. This allows a service object to be given the same OID that it
     * had when it was created in a different session.
     */
    protected abstract Oid getOidForService(String name);

    /**
     * Registers the specified service as having the specified OID.
     */
    protected abstract void registerService(String name, Oid oid);

    public NakedObject getService(final String id) {
        for (final Object service : servicesInjector.getRegisteredServices()) {
            // TODO this (ServiceUtil) uses reflection to access the service object; it should use the
            // reflector, ie call allServices first and use the returned array
            if (id.equals(ServiceUtil.id(service))) {
                return getService(service);
            }
        }
        return null;
    }

    // REVIEW why does this get called multiple times when starting up 
    public List<NakedObject> getServices() {
        List<Object> services = servicesInjector.getRegisteredServices();
        List<NakedObject> serviceAdapters = new ArrayList<NakedObject>();
        for (Object service : services) {
            serviceAdapters.add(getService(service));
        }
        return serviceAdapters;
    }

    private NakedObject getService(final Object service) {
        final Oid oid = getOidForService(ServiceUtil.id(service));
        return recreateAdapterForExistingService(oid, service);
    }

    /**
     * Has any services.
     */
    public boolean hasServices() {
        return servicesInjector.getRegisteredServices().size() > 0;
    }

    private NakedObject recreateAdapterForExistingService(final Oid oid, final Object service) {
        final NakedObject adapter = getAdapterManager().recreateRootAdapter(oid, service);
        if (adapter.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            adapter.changeState(ResolveState.RESOLVING);
            adapter.changeState(ResolveState.RESOLVED);
        }
        return adapter;
    }


    // ////////////////////////////////////////////////////////////////////
    // Helpers
    // ////////////////////////////////////////////////////////////////////
    
	protected boolean isImmutable(final NakedObject adapter) {
		final NakedObjectSpecification noSpec = adapter.getSpecification();
		return SpecificationFacets.isAlwaysImmutable(noSpec) || 
			(SpecificationFacets.isImmutableOncePersisted(noSpec) && 
			 adapter.isPersistent());
	}


    // ////////////////////////////////////////////////////////////////////
    // injectInto
    // ////////////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (PersistenceSessionAware.class.isAssignableFrom(candidate.getClass())) {
            PersistenceSessionAware cast = PersistenceSessionAware.class.cast(candidate);
            cast.setPersistenceSession(this);
        }
        if (PersistenceSessionHydratorAware.class.isAssignableFrom(candidate.getClass())) {
            PersistenceSessionHydratorAware cast = PersistenceSessionHydratorAware.class.cast(candidate);
            cast.setHydrator(this);
        }
    }

    
    // ///////////////////////////////////////////////////////////////////////////
    // Debugging
    // ///////////////////////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.appendTitle(getClass().getName());
        debug.appendln("container", servicesInjector);
        debug.appendln();

        adapterManager.debugData(debug);
        debug.appendln();

        debug.appendln("manually dirtiable support (isDirty flag)?", dirtiableSupport);

        debug.appendTitle("OID Generator");
        oidGenerator.debugData(debug);
        debug.appendln();

        debug.appendTitle("Services");
        for (final Object service : servicesInjector.getRegisteredServices()) {
            final String id = ServiceUtil.id(service);
            final String serviceClassName = service.getClass().getName();
            Oid oidForService = getOidForService(id);
            String serviceId = id + (id.equals(serviceClassName) ? "" : " (" + serviceClassName + ")");
            debug.appendln(oidForService != null? oidForService.toString(): "[NULL]", serviceId);
        }
        debug.appendln();

    }


    // ///////////////////////////////////////////////////////////////////////////
    // Dependencies (injected in constructor, possibly implicitly)
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Injected in constructor.
     */
    public final AdapterFactory getAdapterFactory() {
        return adapterFactory;
    }
    
    /**
     * Injected in constructor.
     */
    public final OidGenerator getOidGenerator() {
        return oidGenerator;
    }

    /**
     * Injected in constructor.
     */
    public final AdapterManagerExtended getAdapterManager() {
        return adapterManager;
    }


    /**
     * The {@link ServicesInjector}. 
     */
    public ServicesInjector getServicesInjector() {
        return servicesInjector;
    }

    /**
     * Obtained indirectly from the injected reflector.
     */
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }


    // ///////////////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    // ///////////////////////////////////////////////////////////////////////////

    protected SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

    
    /**
     * Injects the {@link SpecificationLoader}
     */
    public void setSpecificationLoader(final SpecificationLoader specificationLoader) {
        this.specificationLoader = specificationLoader;
    }




    public void setTransactionManager(final NakedObjectTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public NakedObjectTransactionManager getTransactionManager() {
        return transactionManager;
    }



}
// Copyright (c) Naked Objects Group Ltd.
