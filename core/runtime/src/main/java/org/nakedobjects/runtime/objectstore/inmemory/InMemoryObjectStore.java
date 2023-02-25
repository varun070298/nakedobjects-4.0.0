package org.nakedobjects.runtime.objectstore.inmemory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.debug.Debug;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStoreInstances;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjectsDefault;
import org.nakedobjects.runtime.objectstore.inmemory.internal.commands.InMemoryCreateObjectCommand;
import org.nakedobjects.runtime.objectstore.inmemory.internal.commands.InMemoryDestroyObjectCommand;
import org.nakedobjects.runtime.objectstore.inmemory.internal.commands.InMemorySaveObjectCommand;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionHydrator;
import org.nakedobjects.runtime.persistence.PersistorUtil;
import org.nakedobjects.runtime.persistence.UnsupportedFindException;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class InMemoryObjectStore implements ObjectStore {

    private final static Logger LOG = Logger.getLogger(InMemoryObjectStore.class);

    protected ObjectStorePersistedObjects persistedObjects;

    public InMemoryObjectStore() {
        LOG.info("creating memory object store");
    }

    // ///////////////////////////////////////////////////////
    // Name
    // ///////////////////////////////////////////////////////

    public String name() {
        return "In-Memory Object Store";
    }

    // ///////////////////////////////////////////////////////
    // open, close, shutdown
    // ///////////////////////////////////////////////////////

    public void open() {
        if (persistedObjects == null) {
        	InMemoryPersistenceSessionFactory inMemoryPersistenceSessionFactory = getInMemoryPersistenceSessionFactory();
        	// TODO: all a bit hacky, but is to keep tests running.  Should really sort out using mocks.
        	if (inMemoryPersistenceSessionFactory != null) {
        		persistedObjects = inMemoryPersistenceSessionFactory.createPersistedObjects();
        	} else {
        		persistedObjects = new ObjectStorePersistedObjectsDefault();
        	}
        } else {
            recreateAdapters();
        }
    }

    private void recreateAdapters() {
        for(NakedObjectSpecification noSpec: persistedObjects.specifications()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("recreating adapters for: " + noSpec.getFullName());
			}
        	recreateAdapters(persistedObjects.instancesFor(noSpec));
        }
    }

	private void recreateAdapters(ObjectStoreInstances objectStoreInstances) {
		Map<Oid, Object> objectByOidMap = objectStoreInstances.getObjectInstances();
		for (Oid oid : objectStoreInstances.getOids()) {

		    // it's important not to "touch" the pojo, not even in log messages.  That's because 
		    // the toString() will cause bytecode enhancement to try to resolve references.

			if (LOG.isDebugEnabled()) {
				LOG.debug("recreating adapter: oid=" + oid);
			}
		    Object pojo = objectStoreInstances.getPojo(oid);

		    NakedObject existingAdapterLookedUpByPojo = getAdapterManager().getAdapterFor(pojo);
		    if (existingAdapterLookedUpByPojo != null) {
		    	// this could happen if we rehydrate a persisted object that depends on another persisted object
		    	// not yet rehydrated.
		    	getAdapterManager().removeAdapter(existingAdapterLookedUpByPojo);
		    }

		    NakedObject existingAdapterLookedUpByOid = getAdapterManager().getAdapterFor(oid);
		    ensureThatState(existingAdapterLookedUpByOid, is(nullValue()), "Already have mapping for " + oid);

		    NakedObject recreatedAdapter = getHydrator().recreateAdapter(oid, pojo);
		    
		    Version version = objectStoreInstances.getVersion(oid);
		    recreatedAdapter.setOptimisticLock(version);
		}
	}

	public void close() {
        final InMemoryPersistenceSessionFactory inMemoryPersistenceSessionFactory = getInMemoryPersistenceSessionFactory();
        // TODO: this is hacky, only here to keep tests running.  Should sort out using mocks
        if (inMemoryPersistenceSessionFactory != null) {
        	inMemoryPersistenceSessionFactory.attach(getPersistenceSession(), persistedObjects);
        	persistedObjects = null;
        } 
    }


    // ///////////////////////////////////////////////////////
    // fixtures
    // ///////////////////////////////////////////////////////

    /**
     * No permanent persistence, so must always install fixtures.
     */
    public boolean isFixturesInstalled() {
        return false;
    }

    // ///////////////////////////////////////////////////////
    // reset
    // ///////////////////////////////////////////////////////

    public void reset() {}

    // ///////////////////////////////////////////////////////
    // attach, detach (not API)
    // ///////////////////////////////////////////////////////

    public void attachPersistedObjects(ObjectStorePersistedObjects persistedObjects) {
        this.persistedObjects = persistedObjects;
    }

    // ///////////////////////////////////////////////////////
    // Transaction management
    // ///////////////////////////////////////////////////////

    public void startTransaction() {}

    public void endTransaction() {}

    public void abortTransaction() {}

    // ///////////////////////////////////////////////////////
    // Command Creation
    // ///////////////////////////////////////////////////////

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new InMemoryCreateObjectCommand(object, persistedObjects);
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        return new InMemorySaveObjectCommand(object, persistedObjects);
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        return new InMemoryDestroyObjectCommand(object, persistedObjects);
    }

    // ///////////////////////////////////////////////////////
    // Command Execution
    // ///////////////////////////////////////////////////////

    public void execute(final List<PersistenceCommand> commands) throws ObjectPersistenceException {
        if (LOG.isInfoEnabled()) {
            LOG.info("execute commands");
        }
        for (PersistenceCommand command : commands) {
            command.execute(null);
        }
        LOG.info("end execution");
    }

    // ///////////////////////////////////////////////////////
    // getObject, resolveField, resolveImmediately
    // ///////////////////////////////////////////////////////

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException,
            ObjectPersistenceException {
        LOG.debug("getObject " + oid);
        final ObjectStoreInstances ins = instancesFor(hint);
        final NakedObject object = ins.retrieveObject(oid);
        if (object == null) {
            throw new ObjectNotFoundException(oid);
        } else {
            setupReferencedObjects(object);
            return object;
        }
    }

    public void resolveImmediately(final NakedObject adapter) throws ObjectPersistenceException {

        // this is a nasty hack, but even though this method is called by 
        // PersistenceSessionObjectStore#resolveImmediately which has a check,
    	// seem to be hitting a race condition with another thread that is resolving the object
    	// before I get here.
    	// as belt-n-braces, have also made PSOS#resolveImmediately synchronize on
    	// the object being resolved.
        if (adapter.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            LOG.debug("resolve " + adapter);
            setupReferencedObjects(adapter);
            
        	PersistorUtil.start(adapter, ResolveState.RESOLVING);
        	PersistorUtil.end(adapter); // moves to RESOLVED
        } else {
        	LOG.warn("resolveImmediately ignored, " +
        			 "adapter's current state is: " + adapter.getResolveState() + 
        			 " ; oid: " + adapter.getOid());
        }
    }

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) throws ObjectPersistenceException {
        final NakedObject reference = field.get(object);
        PersistorUtil.start(reference, ResolveState.RESOLVING);
        PersistorUtil.end(reference);
    }

    private void setupReferencedObjects(final NakedObject object) {
        setupReferencedObjects(object, new Vector());
    }

    private void setupReferencedObjects(final NakedObject adapter, final Vector all) {
    	// TODO: is this code needed, then?  Looks like it isn't...
        if (true) {
            return;
        }

        if (adapter == null || all.contains(adapter)) {
            return;
        }
        all.addElement(adapter);
        PersistorUtil.start(adapter, ResolveState.RESOLVING);

        final NakedObjectAssociation[] fields = adapter.getSpecification().getAssociations();
        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation field = fields[i];
            if (field.isOneToManyAssociation()) {
                final NakedObject col = field.get(adapter);
                final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(col);
                for (final Iterator<NakedObject> e = facet.iterator(col); e.hasNext();) {
                    final NakedObject element = e.next();
                    setupReferencedObjects(element, all);
                }
            } else if (field.isOneToOneAssociation()) {
                final NakedObject fieldContent = field.get(adapter);
                setupReferencedObjects(fieldContent, all);
            }
        }

        PersistorUtil.end(adapter);

    }

    // ///////////////////////////////////////////////////////
    // getInstances, hasInstances
    // ///////////////////////////////////////////////////////

    public NakedObject[] getInstances(final PersistenceQuery persistenceQuery) 
    throws ObjectPersistenceException,
            UnsupportedFindException {

    	if (!(persistenceQuery instanceof PersistenceQueryBuiltIn)) {
    		throw new IllegalArgumentException(MessageFormat.format(
							"Provided PersistenceQuery not supported; was {0}; " +
							"the in-memory object store only supports {1}",
							persistenceQuery.getClass().getName(), 
							PersistenceQueryBuiltIn.class.getName()));
    	}
		PersistenceQueryBuiltIn builtIn = (PersistenceQueryBuiltIn) persistenceQuery;
    	
    	final Vector<NakedObject> instances = new Vector<NakedObject>();
        final NakedObjectSpecification spec = persistenceQuery.getSpecification();
        findInstances(spec, builtIn, instances);
        return toInstancesArray(instances);
    }

    public boolean hasInstances(final NakedObjectSpecification spec) {
        if (instancesFor(spec).hasInstances()) {
            return true;
        }
        
        // includeSubclasses
        final NakedObjectSpecification[] subclasses = spec.subclasses();
        for (int i = 0; i < subclasses.length; i++) {
            if (hasInstances(subclasses[i])) {
                return true;
            }
        }
        
        return false;
    }

    private void findInstances(
            final NakedObjectSpecification spec,
            final PersistenceQueryBuiltIn persistenceQuery,
            final Vector<NakedObject> foundInstances) {
    	
        instancesFor(spec).findInstancesAndAdd(persistenceQuery, foundInstances);

        // include subclasses
        final NakedObjectSpecification[] subclasses = spec.subclasses();
        for (int i = 0; i < subclasses.length; i++) {
            findInstances(subclasses[i], persistenceQuery, foundInstances);
        }
        
    }

    private NakedObject[] toInstancesArray(final Vector<NakedObject> instances) {
        final NakedObject[] ins = new NakedObject[instances.size()];
        for (int i = 0; i < ins.length; i++) {
            final NakedObject object = instances.elementAt(i);
            setupReferencedObjects(object);
            if (object.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
                PersistorUtil.start(object, ResolveState.RESOLVING);
                PersistorUtil.end(object);
            }
            ins[i] = object;
        }
        return ins;
    }

    // ///////////////////////////////////////////////////////
    // Services
    // ///////////////////////////////////////////////////////


    public Oid getOidForService(final String name) {
        return persistedObjects.getService(name);
    }

    public void registerService(final String name, final Oid oid) {
    	persistedObjects.registerService(name, oid);
    }

    private ObjectStoreInstances instancesFor(final NakedObjectSpecification spec) {
    	return persistedObjects.instancesFor(spec);
    }

    // ///////////////////////////////////////////////////////
    // Debugging
    // ///////////////////////////////////////////////////////

    public String debugTitle() {
        return name();
    }

    public void debugData(final DebugString debug) {
        debug.appendTitle("Domain Objects");
        for(final NakedObjectSpecification spec: persistedObjects.specifications()) {
            debug.appendln(spec.getFullName());
            final ObjectStoreInstances instances = instancesFor(spec);
            instances.debugData(debug);
        }
        debug.unindent();
        debug.appendln();
    }

    private String debugCollectionGraph(final NakedObject collection, final int level, final Vector recursiveElements) {
        final StringBuffer s = new StringBuffer();

        if (recursiveElements.contains(collection)) {
            s.append("*\n");
        } else {
            recursiveElements.addElement(collection);

            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
            final Iterator<NakedObject> e = facet.iterator(collection);

            while (e.hasNext()) {
                indent(s, level);

                NakedObject element;
                try {
                    element = e.next();
                } catch (final ClassCastException ex) {
                    LOG.error(ex);
                    return s.toString();
                }

                s.append(element);
                s.append(debugGraph(element, level + 1, recursiveElements));
            }
        }

        return s.toString();
    }

    private String debugGraph(final NakedObject object, final int level, final Vector recursiveElements) {
        if (level > 3) {
            return "...\n"; // only go 3 levels?
        }

        Vector elements;
        if (recursiveElements == null) {
            elements = new Vector(25, 10);
        } else {
            elements = recursiveElements;
        }

        if (object.getSpecification().isCollection()) {
            return "\n" + debugCollectionGraph(object, level, elements);
        } else {
            return "\n" + debugObjectGraph(object, level, elements);
        }
    }

    private String debugObjectGraph(final NakedObject object, final int level, final Vector recursiveElements) {
        final StringBuffer s = new StringBuffer();

        recursiveElements.addElement(object);

        // work through all its fields
        NakedObjectAssociation[] fields;

        fields = object.getSpecification().getAssociations();

        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation field = fields[i];
            final Object obj = field.get(object);

            final String id = field.getId();
            indent(s, level);

            if (field.isOneToManyAssociation()) {
                s.append(id + ": \n" + debugCollectionGraph((NakedObject) obj, level + 1, recursiveElements));
            } else {
                if (obj instanceof NakedObject) {
                    if (recursiveElements.contains(obj)) {
                        s.append(id + ": " + obj + "*\n");
                    } else {
                        s.append(id + ": " + obj);
                        s.append(debugGraph((NakedObject) obj, level + 1, recursiveElements));
                    }
                } else {
                    s.append(id + ": " + obj);
                    s.append("\n");
                }
            }
        }

        return s.toString();
    }

    private void indent(final StringBuffer s, final int level) {
        for (int indent = 0; indent < level; indent++) {
            s.append(Debug.indentString(4) + "|");
        }

        s.append(Debug.indentString(4) + "+--");
    }

    

    
    // ///////////////////////////////////////////////////////
    // Dependencies (from context)
    // ///////////////////////////////////////////////////////

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(ObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject the session into not only
     * this object but also the {@link ObjectStoreInstances} that do the work.
     */
    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(ObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject the session into not only
     * this object but also the {@link ObjectStoreInstances} that do the work.
     */
    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(ObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject the session into not only
     * this object but also the {@link ObjectStoreInstances} that do the work.
     */
    private PersistenceSessionHydrator getHydrator() {
        return getPersistenceSession();
    }

    
	/**
	 * Downcasts the {@link PersistenceSessionFactory} to {@link InMemoryPersistenceSessionFactory}.
	 */
	protected InMemoryPersistenceSessionFactory getInMemoryPersistenceSessionFactory() {
		PersistenceSessionFactory persistenceSessionFactory = getPersistenceSession().getPersistenceSessionFactory();

        if (!(persistenceSessionFactory instanceof InMemoryPersistenceSessionFactory)) {
        	// for testing support
            return null;
        }
        return (InMemoryPersistenceSessionFactory) persistenceSessionFactory;
	}


}
// Copyright (c) Naked Objects Group Ltd.
