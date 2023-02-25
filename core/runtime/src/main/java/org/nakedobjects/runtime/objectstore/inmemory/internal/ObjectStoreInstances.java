package org.nakedobjects.runtime.objectstore.inmemory.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.objectstore.inmemory.InMemoryObjectStore;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionHydrator;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;


/*
 * The objects need to store in a repeatable sequence so the elements and instances method return the same data for any repeated
 * call, and so that one subset of instances follows on the previous. This is done by keeping the objects in the order that they
 * where created.
 */
public final class ObjectStoreInstances {
    
    private final Map<Oid, Object> pojoByOidMap = new HashMap<Oid, Object>();
    private final Map<Oid, String> titleByOidMap = new HashMap<Oid, String>();
    private final Map<Oid, SerialNumberVersion> versionByOidMap = new HashMap<Oid, SerialNumberVersion>();
    
	@SuppressWarnings("unused")
	private final NakedObjectSpecification spec;

    
    /////////////////////////////////////////////////////////
    // Constructors
    /////////////////////////////////////////////////////////

    public ObjectStoreInstances(NakedObjectSpecification spec) {
    	this.spec = spec;
    }


    /////////////////////////////////////////////////////////
    // Object Instances
    /////////////////////////////////////////////////////////

    /**
     * TODO: shouldn't really be exposing this directly.
     */
	public Map<Oid, Object> getObjectInstances() {
		return pojoByOidMap;
	}

	public Set<Oid> getOids() {
		return Collections.unmodifiableSet(pojoByOidMap.keySet());
	}

	public Object getPojo(Oid oid) {
		return pojoByOidMap.get(oid);
	}

	public Version getVersion(Oid oid) {
		return versionByOidMap.get(oid);
	}



    /////////////////////////////////////////////////////////
    // shutdown
    /////////////////////////////////////////////////////////

    public void shutdown() {
        pojoByOidMap.clear();
        titleByOidMap.clear();
        versionByOidMap.clear();
    }


    /////////////////////////////////////////////////////////
    // save, remove
    /////////////////////////////////////////////////////////

    public void save(final NakedObject adapter) {
        pojoByOidMap.put(adapter.getOid(), adapter.getObject());
        titleByOidMap.put(adapter.getOid(), adapter.titleString().toLowerCase());
        
        SerialNumberVersion version = versionByOidMap.get(adapter.getOid());
        SerialNumberVersion nextVersion = nextVersion(version);
		versionByOidMap.put(adapter.getOid(), nextVersion);
        adapter.setOptimisticLock(nextVersion);
    }

    private synchronized SerialNumberVersion nextVersion(SerialNumberVersion version) {
    	long sequence = (version != null? version.getSequence(): 0) +1;
		return new SerialNumberVersion(sequence, getAuthenticationSession().getUserName(), new Date(Clock.getTime()));
	}


	public void remove(final Oid oid) {
		pojoByOidMap.remove(oid);
        titleByOidMap.remove(oid);
        versionByOidMap.remove(oid);
    }

    /////////////////////////////////////////////////////////
    // retrieveObject
    /////////////////////////////////////////////////////////

    /**
     * If the pojo exists in the object store, then looks up the
     * {@link NakedObject adapter} from the {@link AdapterManager}, and only
     * if none found does it {@link PersistenceSessionHydrator#recreateAdapter(Oid, Object) recreate}
     * a new {@link NakedObject adapter}.
     */
    public NakedObject retrieveObject(final Oid oid) {
        final Object pojo = getObjectInstances().get(oid);
        if (pojo == null) {
            return null;
        }
        NakedObject adapterLookedUpByPojo = getAdapterManager().getAdapterFor(pojo);
        if (adapterLookedUpByPojo != null) {
            return adapterLookedUpByPojo;
        }
        NakedObject adapterLookedUpByOid = getAdapterManager().getAdapterFor(oid);
        if (adapterLookedUpByOid != null) {
            return adapterLookedUpByOid;
        }
        return getHydrator().recreateAdapter(oid, pojo);
    }


    
    /////////////////////////////////////////////////////////
    // instances, numberOfInstances, hasInstances
    /////////////////////////////////////////////////////////

    /**
     * Not API, but <tt>public</tt> so can be called by {@link InMemoryObjectStore}.
     */
    public void findInstancesAndAdd(final PersistenceQueryBuiltIn persistenceQuery, final Vector<NakedObject> foundInstances) {
        if (persistenceQuery instanceof PersistenceQueryFindByTitle) {
            for (final Oid oid : titleByOidMap.keySet()) {
                final String title = titleByOidMap.get(oid);
                if (((PersistenceQueryFindByTitle) persistenceQuery).matches(title)) {
                    final NakedObject adapter = retrieveObject(oid);
                    foundInstances.add(adapter);
                }
            }
            return;
        }

        for (final NakedObject element : elements()) {
            if (persistenceQuery.matches(element)) {
                foundInstances.addElement(element);
            }
        }
    }

    public int numberOfInstances() {
        return getObjectInstances().size();
    }

    public boolean hasInstances() {
        return numberOfInstances() > 0;
    }


    private List<NakedObject> elements() {
        final List<NakedObject> v = new ArrayList<NakedObject>(getObjectInstances().size());
        for (final Oid oid : getObjectInstances().keySet()) {
            v.add(retrieveObject(oid));
        }
        return v;
    }


    /////////////////////////////////////////////////////////
    // Debugging
    /////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.indent();
        if (getObjectInstances().size() == 0) {
            debug.appendln("no instances");
        }
        for (final Oid oid : getObjectInstances().keySet()) {
            final String title = titleByOidMap.get(oid);
            final Object object = getObjectInstances().get(oid);
            debug.appendln(oid.toString(), object + " (" + title + ")");
        }
        debug.appendln();
        debug.unindent();
    }



    // ///////////////////////////////////////////////////////
    // Dependencies (from context)
    // ///////////////////////////////////////////////////////

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(MemoryObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject this object via the
     * {@link InMemoryObjectStore}.
     */
    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(MemoryObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject this object via the
     * {@link InMemoryObjectStore}.
     */
    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    /**
     * Must use {@link NakedObjectsContext context}, because although this object is recreated with each
     * {@link PersistenceSession session}, the persisted objects that get
     * {@link #attachPersistedObjects(MemoryObjectStorePersistedObjects) attached} to it span multiple
     * sessions.
     * 
     * <p>
     * The alternative design would be to laboriously inject this object via the
     * {@link InMemoryObjectStore}.
     */
    private PersistenceSessionHydrator getHydrator() {
        return getPersistenceSession();
    }



	private static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}




}
// Copyright (c) Naked Objects Group Ltd.
