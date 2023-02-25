package org.nakedobjects.runtime.persistence.objectstore;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacet;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.FixturesInstalledFlag;
import org.nakedobjects.runtime.persistence.NotPersistableException;
import org.nakedobjects.runtime.persistence.PersistenceSessionAbstract;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.ObjectStoreTransactionManager;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;
import org.nakedobjects.runtime.transaction.TransactionalClosureWithReturnAbstract;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

public class PersistenceSessionObjectStore extends PersistenceSessionAbstract
		implements ToPersistObjectSet {
	private static final Logger LOG = Logger
			.getLogger(PersistenceSessionObjectStore.class);
	private final PersistAlgorithm persistAlgorithm;
	private final ObjectStorePersistence objectStore;

	/**
	 * Initialize the object store so that calls to this object store access
	 * persisted objects and persist changes to the object that are saved.
	 */
	public PersistenceSessionObjectStore(
			final PersistenceSessionFactory persistenceSessionFactory,
			final AdapterFactory adapterFactory,
			final ObjectFactory objectFactory,
			final ServicesInjector servicesInjector,
			final OidGenerator oidGenerator,
			final AdapterManagerExtended identityMap,
			final PersistAlgorithm persistAlgorithm,
			final ObjectStorePersistence objectStore) {

		super(persistenceSessionFactory, adapterFactory, objectFactory,
				servicesInjector, oidGenerator, identityMap);
		if (LOG.isDebugEnabled()) {
			LOG.debug("creating " + this);
		}

		ensureThatArg(persistAlgorithm, is(not(nullValue())),
				"persist algorithm required");
		ensureThatArg(objectStore, is(not(nullValue())),
				"object store required");

		this.persistAlgorithm = persistAlgorithm;
		this.objectStore = objectStore;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// init, shutdown
	// ///////////////////////////////////////////////////////////////////////////

	@Override
	protected void doOpen() {

		ensureThatState(objectStore, is(notNullValue()),
				"object store required");
		ensureThatState(getTransactionManager(), is(notNullValue()),
				"transaction manager required");
		ensureThatState(persistAlgorithm, is(notNullValue()),
				"persist algorithm required");

		this.injectInto(objectStore); // as a hydrator
		getAdapterManager().injectInto(objectStore);
		getSpecificationLoader().injectInto(objectStore);
		getTransactionManager().injectInto(objectStore);

		getOidGenerator().injectInto(objectStore);

		objectStore.open();
	}

	/**
	 * Returns the cached value of {@link ObjectStore#isFixturesInstalled()
	 * whether fixtures are installed} from the
	 * {@link PersistenceSessionFactory} (provided it implements
	 * {@link FixturesInstalledFlag}), otherwise queries {@link ObjectStore}
	 * directly.
	 * <p>
	 * This caching is important because if we've determined, for a given run,
	 * that fixtures are not installed, then we don't want to change our mind by
	 * asking the object store again in another session.
	 * 
	 * @see FixturesInstalledFlag
	 */
	public boolean isFixturesInstalled() {
		PersistenceSessionFactory persistenceSessionFactory = getPersistenceSessionFactory();
		if (persistenceSessionFactory instanceof FixturesInstalledFlag) {
			FixturesInstalledFlag fixturesInstalledFlag = (FixturesInstalledFlag) persistenceSessionFactory;
			if (fixturesInstalledFlag.isFixturesInstalled() == null) {
				fixturesInstalledFlag.setFixturesInstalled(objectStore
						.isFixturesInstalled());
			}
			return fixturesInstalledFlag.isFixturesInstalled();
		} else {
			return objectStore.isFixturesInstalled();
		}
	}

	@Override
	protected void doClose() {
		objectStore.close();
	}

	@Override
	public void testReset() {
		objectStore.reset();
		getAdapterManager().reset();
		super.testReset();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		LOG.info("finalizing object manager");
	}

	// ///////////////////////////////////////////////////////////////////////////
	// loadObject, reload
	// ///////////////////////////////////////////////////////////////////////////

	public NakedObject loadObject(final Oid oid,
			final NakedObjectSpecification hintSpec) {
		ensureThatArg(oid, is(notNullValue()));
		ensureThatArg(hintSpec, is(notNullValue()));

		NakedObject adapter = getAdapterManager().getAdapterFor(oid);
		if (adapter != null) {
			return adapter;
		}

		return loadObjectFromPersistenceLayer(oid, hintSpec);
	}

	private NakedObject loadObjectFromPersistenceLayer(final Oid oid,
			final NakedObjectSpecification hintSpec) {
		// the object store will map for us, using its hydrator (calls back
		// to #recreateAdapter)
		return getTransactionManager().executeWithinTransaction(
			new TransactionalClosureWithReturnAbstract<NakedObject>(){
				public NakedObject execute() {
					return objectStore.getObject(oid, hintSpec);
				}});
	}

	/**
	 * Does nothing.
	 */
	public void reload(final NakedObject object) {
	}

	// ///////////////////////////////////////////////////////////////////////////
	// resolveImmediately, resolveField
	// ///////////////////////////////////////////////////////////////////////////

	public void resolveImmediately(final NakedObject adapter) {
		// synchronize on the current session because getting race
		// conditions, I think between different UI threads when running
		// with DnD viewer + in-memory object store +
		// cglib bytecode enhancement
		synchronized (getAuthenticationSession()) {
			final ResolveState resolveState = adapter.getResolveState();
			if (!resolveState.canChangeTo(ResolveState.RESOLVING)) {
				return;
			}
			Assert.assertFalse("only resolve object that is not yet resolved",
					adapter, resolveState.isResolved());
			Assert.assertTrue("only resolve object that is persistent",
					adapter, adapter.isPersistent());
			if (LOG.isInfoEnabled()) {
				// don't log object - its toString() may use the
				// unresolved field, or unresolved collection
				LOG.info("resolve immediately: "
						+ adapter.getSpecification().getShortName() + " "
						+ resolveState.code() + " " + adapter.getOid());
			}
			resolveImmediatelyFromPersistenceLayer(adapter);
		}
	}

	private void resolveImmediatelyFromPersistenceLayer(final NakedObject adapter) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract() {
			public void preExecute() {
				CallbackUtils.callCallback(adapter,
						LoadingCallbackFacet.class);
			}
			public void execute() {
				objectStore.resolveImmediately(adapter);
			}
			public void onSuccess() {
				CallbackUtils.callCallback(adapter,
						LoadedCallbackFacet.class);
			}
			public void onFailure() {
				// TODO: should we do something here?
			}
		});
	}

	public void resolveField(final NakedObject objectAdapter,
			final NakedObjectAssociation field) {
		if (field.getSpecification().isCollectionOrIsAggregated()) {
			return;
		}
		final NakedObject referenceAdapter = field.get(objectAdapter);
		if (referenceAdapter == null
				|| referenceAdapter.getResolveState().isResolved()) {
			return;
		}
		if (!referenceAdapter.isPersistent()) {
			return;
		}
		if (LOG.isInfoEnabled()) {
			// don't log object - it's toString() may use the unresolved field
			// or unresolved collection
			LOG.info("resolve field "
					+ objectAdapter.getSpecification().getShortName() + "."
					+ field.getId() + ": "
					+ referenceAdapter.getSpecification().getShortName() + " "
					+ referenceAdapter.getResolveState().code() + " "
					+ referenceAdapter.getOid());
		}
		resolveFieldFromPersistenceLayer(objectAdapter, field);
	}

	private void resolveFieldFromPersistenceLayer(final NakedObject objectAdapter,
			final NakedObjectAssociation field) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract() {
			public void execute() {
				objectStore.resolveField(objectAdapter, field);
			}});
	}

    //////////////////////////////////////////////////////////////////
    // makePersistent
    //////////////////////////////////////////////////////////////////

	/**
	 * Makes a naked object persistent. The specified object should be stored
	 * away via this object store's persistence mechanism, and have an new and
	 * unique OID assigned to it. The object, should also be added to the
	 * {@link AdapterManager} as the object is implicitly 'in use'.
	 * 
	 * <p>
	 * If the object has any associations then each of these, where they aren't
	 * already persistent, should also be made persistent by recursively calling
	 * this method.
	 * 
	 * <p>
	 * If the object to be persisted is a collection, then each element of that
	 * collection, that is not already persistent, should be made persistent by
	 * recursively calling this method.
	 * 
	 * @see #remapAsPersistent(NakedObject)
	 */
	public void makePersistent(final NakedObject adapter) {
		if (adapter.isPersistent()) {
			throw new NotPersistableException("Object already persistent: "
					+ adapter);
		}
		if (!adapter.getSpecification().persistability().isPersistable()) {
			throw new NotPersistableException("Object is not persistable: "
					+ adapter);
		}
		final NakedObjectSpecification specification = adapter
				.getSpecification();
		if (specification.isService()) {
			throw new NotPersistableException("Cannot persist services: "
					+ adapter);
		}

		makePersistentInPersistenceLayer(adapter);
	}

	protected void makePersistentInPersistenceLayer(final NakedObject adapter) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			@Override
			public void preExecute() {
				CallbackUtils.callCallback(adapter, PersistingCallbackFacet.class);
			}
			public void execute() {
				persistAlgorithm.makePersistent(
						adapter, PersistenceSessionObjectStore.this);
			}
			@Override
			public void onSuccess() {
				CallbackUtils.callCallback(adapter, PersistedCallbackFacet.class);
			}
			@Override
			public void onFailure() {
				// TODO: some sort of callback?
			}
			});
	}

	// ///////////////////////////////////////////////////////////////////////////
	// objectChanged
	// ///////////////////////////////////////////////////////////////////////////

	public void objectChanged(final NakedObject adapter) {

		if (adapter.isTransient()) {
			addObjectChangedForPresentationLayer(adapter);
			return;
		}

		if (adapter.getResolveState().respondToChangesInPersistentObjects()) {
			if (isImmutable(adapter)) {
				// previously used to throw 
				// new ObjectPersistenceException("cannot change immutable object");
				// however, since the the bytecode enhancers effectively make
				// calling objectChanged() the responsibility of the framework, 
				// we may as well now do the check here and ignore if doesn't apply.
				return;
			}
			
			addObjectChangedForPersistenceLayer(adapter);
			addObjectChangedForPresentationLayer(adapter);
		}
		if (adapter.getResolveState().respondToChangesInPersistentObjects() || adapter.isTransient()) {
			addObjectChangedForPresentationLayer(adapter);
		}
	}

	private void addObjectChangedForPresentationLayer(final NakedObject adapter) {
		adapter.fireChangedEvent();
		getUpdateNotifier().addChangedObject(adapter);
	}

	private void addObjectChangedForPersistenceLayer(final NakedObject adapter) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			@Override
			public void preExecute() {
				CallbackUtils.callCallback(adapter, UpdatingCallbackFacet.class);
			}
			public void execute() {
				SaveObjectCommand saveObjectCommand = 
					objectStore.createSaveObjectCommand(adapter);
				getTransactionManager().addCommand(saveObjectCommand);
			}

			public void onSuccess() {
				CallbackUtils.callCallback(adapter, UpdatedCallbackFacet.class);
			}
			
			public void onFailure() {
				// TODO: should we do something here?
			}
		});
		getUpdateNotifier().addChangedObject(adapter);
	}


	// ///////////////////////////////////////////////////////////////////////////
	// destroyObject
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Removes the specified object from the system. The specified object's data
	 * should be removed from the persistence mechanism.
	 */
	public void destroyObject(final NakedObject adapter) {
		if (LOG.isInfoEnabled()) {
			LOG.info("destroyObject " + adapter);
		}

		destroyObjectInPersistenceLayer(adapter);
	}

	private void destroyObjectInPersistenceLayer(final NakedObject adapter) {
		getTransactionManager().executeWithinTransaction(new TransactionalClosureAbstract(){
			@Override
			public void preExecute() {
				CallbackUtils.callCallback(adapter, RemovingCallbackFacet.class);
			}
			public void execute() {
				final DestroyObjectCommand command = 
					objectStore.createDestroyObjectCommand(adapter);
				getTransactionManager().addCommand(command);
			}
			public void onSuccess() {
				CallbackUtils.callCallback(adapter, RemovedCallbackFacet.class);
			}
			public void onFailure() {
				// TODO: some sort of callback?
			}
			});
	}


	
	// ///////////////////////////////////////////////////////////////////////////
	// remapAsPersistent
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Callback from the {@link PersistAlgorithm} (or equivalent; some object
	 * stores such as Hibernate will use listeners instead) to indicate that the
	 * {@link NakedObject adapter} is persisted, and the adapter maps should be
	 * updated.
	 * 
	 * <p>
	 * The object store is expected to have already updated the {@link Oid}
	 * state and the {@link ResolveState} . Some object stores (again, we're
	 * thinking Hibernate here) might also have updated collections, both the
	 * Oid of the collection and the pojo wrapped by the adapter.
	 * 
	 * <p>
	 * The {@link PersistAlgorithm} is called from
	 * {@link #makePersistent(NakedObject)}.
	 * 
	 * <p>
	 * TODO: the <tt>PersistenceSessionProxy</tt> doesn't have this method; should
	 * document better why this is the case, and where the equivalent functionality
	 * is (somewhere in the marshalling stuff, I think).
	 * 
	 * @see #remapAsPersistent(NakedObject)
	 */
	public void remapAsPersistent(final NakedObject adapter) {
		getAdapterManager().remapAsPersistent(adapter);
	}


	// ///////////////////////////////////////////////////////////////////////////
	// getInstances
	// ///////////////////////////////////////////////////////////////////////////

	@Override
	protected NakedObject[] getInstances(final PersistenceQuery persistenceQuery) {
		if (LOG.isInfoEnabled()) {
			LOG.info("getInstances matching " + persistenceQuery);
		}
		return getInstancesFromPersistenceLayer(persistenceQuery);
	}

	private NakedObject[] getInstancesFromPersistenceLayer(
			final PersistenceQuery persistenceQuery) {
		return getTransactionManager().executeWithinTransaction(
			new TransactionalClosureWithReturnAbstract<NakedObject[]>(){
				public NakedObject[] execute() {
					return objectStore.getInstances(persistenceQuery);
				}
				@Override
				public void onSuccess() {
					clearAllDirty();
				}
			});
	}

	
	// ///////////////////////////////////////////////////////////////////////////
	// hasInstances
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Checks whether there are any instances of the specified type. The object
	 * store should look for instances of the type represented by <variable>type
	 * </variable> and return <code>true</code> if there are, or
	 * <code>false</code> if there are not.
	 */
	public boolean hasInstances(final NakedObjectSpecification specification) {
		if (LOG.isInfoEnabled()) {
			LOG.info("hasInstances of " + specification.getShortName());
		}
		return hasInstancesFromPersistenceLayer(specification);
	}

	private boolean hasInstancesFromPersistenceLayer(
			final NakedObjectSpecification specification) {
		return getTransactionManager().executeWithinTransaction(
				new TransactionalClosureWithReturnAbstract<Boolean>(){
					public Boolean execute() {
						return objectStore.hasInstances(specification);
					}});
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Services
	// ///////////////////////////////////////////////////////////////////////////

	@Override
	protected Oid getOidForService(final String name) {
		return getOidForServiceFromPersistenceLayer(name);
	}

	private Oid getOidForServiceFromPersistenceLayer(final String name) {
		return objectStore.getOidForService(name);
	}

	@Override
	protected void registerService(final String name, final Oid oid) {
		objectStore.registerService(name, oid);
	}

	// ///////////////////////////////////////////////////////////////////////////
	// TransactionManager
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Just downcasts.
	 */
	public ObjectStoreTransactionManager getTransactionManager() {
		return (ObjectStoreTransactionManager) super.getTransactionManager();
	}

	/**
	 * Uses the {@link ObjectStore} to
	 * {@link ObjectStore#createCreateObjectCommand(NakedObject) create} a
	 * {@link CreateObjectCommand}, and adds to the
	 * {@link NakedObjectTransactionManager}.
	 */
	public void addPersistedObject(final NakedObject object) {
		getTransactionManager().addCommand(
				objectStore.createCreateObjectCommand(object));
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Debugging
	// ///////////////////////////////////////////////////////////////////////////

	@Override
	public void debugData(final DebugString debug) {
		super.debugData(debug);

		debug.appendTitle("Persistor");
		getTransactionManager().debugData(debug);
		debug.appendln("Persist Algorithm", persistAlgorithm);
		debug.appendln("Object Store", objectStore);
		debug.appendln();

		objectStore.debugData(debug);
	}

	public String debugTitle() {
		return "Object Store Persistor";
	}

	@Override
	public String toString() {
		final ToString toString = new ToString(this);
		if (objectStore != null) {
			toString.append("objectStore", objectStore.name());
		}
		if (persistAlgorithm != null) {
			toString.append("persistAlgorithm", persistAlgorithm.name());
		}
		return toString.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Dependencies
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Injected by constructor.
	 */
	public ObjectStorePersistence getObjectStore() {
		return objectStore;
	}

	/**
	 * Injected by constructor.
	 */
	public PersistAlgorithm getPersistAlgorithm() {
		return persistAlgorithm;
	}

	private UpdateNotifier getUpdateNotifier() {
		return getTransactionManager().getTransaction().getUpdateNotifier();
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Dependencies (from context)
	// ///////////////////////////////////////////////////////////////////////////

	private static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}
	

}
// Copyright (c) Naked Objects Group Ltd.
