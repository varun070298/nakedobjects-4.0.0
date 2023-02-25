package org.nakedobjects.runtime.testsystem;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;


public class TestProxyIdentityMapOriginal implements AdapterManagerExtended {
    
    private final Hashtable<Oid,NakedObject> identities = new Hashtable<Oid,NakedObject>();
    private final Hashtable<Object,NakedObject> objectAdapters = new Hashtable<Object,NakedObject>();
    private final Hashtable<Object,NakedObject> collectionAdapters = new Hashtable<Object,NakedObject>();
    private final Hashtable<Oid,NakedObject> recreatedPersistent = new Hashtable<Oid,NakedObject>();
    private final Vector recreatedTransient = new Vector();
    private final Hashtable valueAdapters = new Hashtable();

    public void addAdapter(final Object object, final NakedObject nakedobject) {
        if (nakedobject.getSpecification().isCollection()) {
            collectionAdapters.put(object, nakedobject);
        } else {
            objectAdapters.put(object, nakedobject);
        }
    }

    public void addIdentity(final Oid oid, final NakedObject adapter) {
        identities.put(oid, adapter);
    }

    public void addRecreated(final Oid oid, final NakedObject adapter) {
        recreatedPersistent.put(oid, adapter);
    }

    public void addRecreatedTransient(final NakedObject adapter) {
        recreatedTransient.addElement(adapter);
    }

    public NakedObject getAdapterFor(final Object object) {
        return (NakedObject) objectAdapters.get(object);
    }

    public NakedObject getAdapterFor(final Oid oid) {
        final NakedObject no = (NakedObject) identities.get(oid);
        return no;
    }

    public Iterator<NakedObject> iterator() {
        throw new NakedObjectException();
    }

    public void open() {}

    public void initDomainObject(final Object domainObject) {}

    public boolean isIdentityKnown(final Oid oid) {
        return identities.containsKey(oid);
    }

    public void remapAsPersistent(final NakedObject adapter) {
        final Oid oid = adapter.getOid();
        getOidGenerator().convertTransientToPersistentOid(oid);
        remapUpdated(oid);
        adapter.changeState(ResolveState.RESOLVED);
    }

    public void remapUpdated(Oid oid) {
        identities.remove(oid);
        Oid previousOid = oid.getPrevious();
        final NakedObject object = (NakedObject) identities.get(previousOid);
        if (object == null) {
            return;
        }
        identities.remove(previousOid);
        final Oid oidFromObject = object.getOid();
        oidFromObject.copyFrom(oid);
        identities.put(oidFromObject, object);
    }

    public void reset() {
        // collectionAdapters.clear();
        identities.clear();
        objectAdapters.clear();
        recreatedPersistent.clear();
        recreatedTransient.clear();
        valueAdapters.clear();
    }

    public void close() {}

    public void unloaded(final NakedObject object) {
        throw new NakedObjectException();
    }

    public NakedObject addAdapter(final NakedObject adapter) {
        identities.put(adapter.getOid(), adapter);
        objectAdapters.put(adapter.getObject(), adapter);
        
        return adapter;
    }

    public NakedObject createAdapter(Object pojo, Oid oid) {
        throw new NotYetImplementedException();
    }

    public NakedObject adapterFor(Object pojo, NakedObject ownerAdapter, Identified identified) {
        throw new NotYetImplementedException();
    }

    public NakedObject adapterFor(Object pojo) {
        throw new NotYetImplementedException();
    }

    public NakedObject adapterFor(Object pojo, Oid oid) {
        throw new NotYetImplementedException();
    }

    public void removeAdapter(NakedObject objectToDispose) {
        throw new NotYetImplementedException();
    }

    public void setAdapterFactory(AdapterFactory adapterFactory) {
        throw new NotYetImplementedException();
    }

    public void setSpecificationLoader(SpecificationLoader specificationLoader) {
        throw new NotYetImplementedException();
    }

    public void setOidGenerator(OidGenerator oidGenerator) {
        throw new NotYetImplementedException();
    }

    public NakedObject addExistingAdapter(NakedObject adapter) {
        return addAdapter(adapter);
    }

    public NakedObject testCreateAdapterFor(Object pojo, Oid oid) {
        return recreateRootAdapter(oid, pojo);
    }

    public NakedObject recreateRootAdapter(Oid oid, Object pojo) {
        throw new NotYetImplementedException();
    }

    public NakedObject testCreateTransient(Object pojo, Oid oid) {
        throw new NotYetImplementedException();
    }

    
    public void injectInto(Object candidate) {
        throw new NotYetImplementedException();
    }

    public String debugTitle() {
        return "Test Proxy Identity Map";
    }

    public void debugData(final DebugString debug) {}


    
    ////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////////////

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private OidGenerator getOidGenerator() {
        return getPersistenceSession().getOidGenerator();
    }

	public void removeAdapter(Oid oid) {
		NakedObject adapter = getAdapterFor(oid);
		if (adapter != null) {
			removeAdapter(adapter);
		}
	}



    

}
// Copyright (c) Naked Objects Group Ltd.
