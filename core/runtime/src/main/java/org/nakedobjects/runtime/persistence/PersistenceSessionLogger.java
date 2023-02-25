package org.nakedobjects.runtime.persistence;

import java.util.List;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.logging.Logger;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


public class PersistenceSessionLogger extends Logger implements PersistenceSession, DebugInfo {
    
    private final PersistenceSession underlying;

    public PersistenceSessionLogger(final PersistenceSession decorated, final String logFileName) {
        super(logFileName, false);
        this.underlying = decorated;
    }

    public PersistenceSessionLogger(final PersistenceSession decorated) {
        super(null, false);
        this.underlying = decorated;
    }

    public void destroyObject(final NakedObject object) {
        log("destroy " + object.getObject());
        underlying.destroyObject(object);
    }

    public NakedObject findInstances(Query query, QueryCardinality cardinality) throws UnsupportedFindException {
        log("find instances matching " + query.getDescription());
        return underlying.findInstances(query, cardinality);
    }


	public NakedObject findInstances(PersistenceQuery criteria) {
		log("find instances matching " + criteria.getSpecification());
		return underlying.findInstances(criteria);
	}

    public void debugData(final DebugString debug) {
        if (underlying instanceof DebugInfo) {
            ((DebugInfo) underlying).debugData(debug);
        }
    }

    public String debugTitle() {
        if (underlying instanceof DebugInfo) {
            return ((DebugInfo) underlying).debugTitle();
        } else {
            return "";
        }
    }

    @Override
    protected Class<?> getDecoratedClass() {
        return underlying.getClass();
    }

    public NakedObject loadObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException {
        final NakedObject object = underlying.loadObject(oid, hint);
        log("get object for " + oid + " (of type " + hint.getShortName() + ")", object.getObject());
        return object;
    }

    public boolean hasInstances(final NakedObjectSpecification specification) {
        final boolean hasInstances = underlying.hasInstances(specification);
        log("has instances of " + specification.getShortName(), "" + hasInstances);
        return hasInstances;
    }

    public boolean isFixturesInstalled() {
        final boolean isInitialized = underlying.isFixturesInstalled();
        log("is initialized: " + isInitialized);
        return isInitialized;
    }


    public void open() {
        log("opening " + underlying);
        underlying.open();
    }

    public void close() {
        log("closing " + underlying);
        underlying.close();
    }

    public void makePersistent(final NakedObject object) {
        log("make object graph persistent: " + object);
        underlying.makePersistent(object);
    }

    public void objectChanged(final NakedObject object) {
        log("object changed " + object);
        underlying.objectChanged(object);
    }

    public void reload(final NakedObject object) {
        underlying.reload(object);
        log("reload: " + object);
    }

    public void testReset() {
        log("reset object manager");
        underlying.testReset();
    }

    public void resolveImmediately(final NakedObject object) {
        underlying.resolveImmediately(object);
        log("Resolve immediately: " + object);
    }

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) {
        log("resolve eagerly object in field " + field + " of " + object);
        underlying.resolveField(object, field);
    }

    public void objectChangedAllDirty() {
        log("saving changes");
        underlying.objectChangedAllDirty();
    }


    public NakedObject getService(final String id) {
        log("get service " + id);
        return underlying.getService(id);
    }

    public List<NakedObject> getServices() {
        log("get services ");
        return underlying.getServices();
    }


    public NakedObject createInstance(final NakedObjectSpecification specification) {
        log("create instance " + specification);
        return underlying.createInstance(specification);
    }

    public NakedObject recreateAdapter(final Oid oid, final NakedObjectSpecification specification) {
        log("recreate instance " + oid + " " + specification);
        return underlying.recreateAdapter(oid, specification);
    }

    public void setSpecificationLoader(final SpecificationLoader specificationLoader) {
        underlying.setSpecificationLoader(specificationLoader);
    }

    public OidGenerator getOidGenerator() {
        return underlying.getOidGenerator();
    }

    public AdapterFactory getAdapterFactory() {
        return underlying.getAdapterFactory();
    }

    public PersistenceSessionFactory getPersistenceSessionFactory() {
        return underlying.getPersistenceSessionFactory();
    }

    public ServicesInjector getServicesInjector() {
        return underlying.getServicesInjector();
    }

    public NakedObjectTransactionManager getTransactionManager() {
        return underlying.getTransactionManager();
    }

    public void setTransactionManager(NakedObjectTransactionManager transactionManager) {
        underlying.setTransactionManager(transactionManager);
    }

	public ObjectFactory getObjectFactory() {
		return underlying.getObjectFactory();
	}

    public void clearAllDirty() {
        underlying.clearAllDirty();
    }

    public NakedObject reload(Oid oid) {
        return underlying.reload(oid);
    }

    public AdapterManager getAdapterManager() {
        return underlying.getAdapterManager();
    }

    public NakedObject recreateAdapter(Oid oid, Object pojo) {
        return underlying.recreateAdapter(oid, pojo);
    }

    public void injectInto(Object candidate) {
        underlying.injectInto(candidate);
    }


}
// Copyright (c) Naked Objects Group Ltd.
