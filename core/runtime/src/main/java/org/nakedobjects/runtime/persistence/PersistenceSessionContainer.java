package org.nakedobjects.runtime.persistence;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

/**
 * PersistenceSession as it appears to most typical client-side code.
 */
public interface PersistenceSessionContainer {

    
    ///////////////////////////////////////////////////////////
    // Creation
    ///////////////////////////////////////////////////////////
    
    /**
     * Creates a new instance of the specified type and returns it in an adapter whose resolved state set to
     * {@link ResolveState#TRANSIENT} (except if the type is marked as {@link NakedObjectSpecification#isValueOrIsAggregated() aggregated}
     * in which case it will be set to {@link ResolveState#VALUE}).
     * 
     * <p>
     * <b><i>
     * REVIEW: not sure about {@link ResolveState#VALUE} - see comments in 
     * {@link #adapterFor(Object, Oid, Version)}.</i></b>
     * 
     * <p>
     * While creating the object the it will be initialised with default values and its created
     * lifecycle method (its logical constructor) will be invoked.  Contrast this with
     * {@link #recreateTransientInstance(Oid, NakedObjectSpecification)}.
     * 
     * <p>
     * This method is ultimately delegated to by the {@link DomainObjectContainer}.
     */
    NakedObject createInstance(NakedObjectSpecification specification);



    ///////////////////////////////////////////////////////////
    // Finding
    ///////////////////////////////////////////////////////////


    /**
     * Loads the object identified by the specified {@link Oid} from the persisted 
     * set of objects.
     * 
     * <p>
     * As used primarily by <tt>ServerDistribution</tt>.
     */
    NakedObject loadObject(Oid oid, NakedObjectSpecification spec);


    /**
     * Finds and returns instances that match the specified query.
     * 
     * <p>
     * The {@link QueryCardinality} determines whether all instances or just the first matching instance is returned.
     * 
     * @throws UnsupportedFindException
     *             if the criteria is not support by this persistor
     */
    <T> NakedObject findInstances(Query<T> query, QueryCardinality cardinality);

    /**
     * Finds and returns instances that match the specified {@link PersistenceQuery}.
     * 
     * <p>
     * Compared to {@link #findInstances(Query, QueryCardinality)}, not that there is no
     * {@link QueryCardinality} parameter.  That's because {@link PersistenceQuery} intrinsically
     * carry the knowledge as to how many rows they return.
     * 
     * @throws UnsupportedFindException
     *             if the criteria is not support by this persistor
     */
    NakedObject findInstances(PersistenceQuery criteria);
    
    /**
     * Whether there are any instances of the specified {@link NakedObjectSpecification type}.
     * 
     * <p>
     * Used (ostensibly) by client-side code.
     */
    boolean hasInstances(NakedObjectSpecification specification);



    ///////////////////////////////////////////////////////////
    // Resolving
    ///////////////////////////////////////////////////////////

    /**
     * Re-initialises the fields of an object. If the object is unresolved then the object's missing data
     * should be retrieved from the persistence mechanism and be used to set up the value objects and
     * associations.
     */
    void resolveImmediately(NakedObject object);
    
    /**
     * Hint that specified field within the specified object is likely to be needed soon. This allows the
     * object's data to be loaded, ready for use.
     * 
     * <p>
     * This method need not do anything, but offers the object store the opportunity to load in objects before
     * their use. Contrast this with resolveImmediately, which requires an object to be loaded before
     * continuing.
     * 
     * @see #resolveImmediately(NakedObject)
     */
    void resolveField(NakedObject object, NakedObjectAssociation association);

    
    ///////////////////////////////////////////////////////////
    // Persisting
    ///////////////////////////////////////////////////////////
    
    /**
     * Makes a naked object persistent. The specified object should be stored away via this object store's
     * persistence mechanism, and have an new and unique OID assigned to it (by calling the object's
     * <code>setOid</code> method). The object, should also be added to the cache as the object is implicitly
     * 'in use'.
     * 
     * <p>
     * If the object has any associations then each of these, where they aren't already persistent, should
     * also be made persistent by recursively calling this method.
     * </p>
     * 
     * <p>
     * If the object to be persisted is a collection, then each element of that collection, that is not
     * already persistent, should be made persistent by recursively calling this method.
     * </p>
     */
    void makePersistent(NakedObject object);

    /**
     * Mark the {@link NakedObject} as changed, and therefore requiring flushing
     * to the persistence mechanism.
     */
    void objectChanged(NakedObject object);

    void destroyObject(NakedObject object);

}


// Copyright (c) Naked Objects Group Ltd.
