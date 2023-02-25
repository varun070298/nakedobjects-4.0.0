package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;


/**
 * Naked objects are adapters to domain objects, where the application is written in terms of domain objects
 * and those objects are represented within the NOF through these adapter, and not directly.
 * 
 * @see NakedObjectMM
 */
public interface NakedObject extends NakedObjectMM {

    /**
     * Returns the name of an icon to use if this object is to be displayed graphically.
     * 
     * <p>
     * May return <code>null</code> if no icon is specified.
     */
    String getIconName();

    /**
     * Changes the 'lazy loaded' state of the domain object.
     * 
     * @see ResolveState
     */
    void changeState(ResolveState newState);

    /**
     * Checks the version of this adapter to make sure that it does not differ from the specified version.
     * 
     * @throws ConcurrencyException
     *             if the specified version differs from the version held this adapter.
     */
    void checkLock(Version version);

    /**
     * The objects unique id. This id allows the object to added to, stored by, and retrieved from the object
     * store.
     */
    Oid getOid();

    /**
     * Determines what 'lazy loaded' state the domain object is in.
     * 
     * @see ResolveState
     */
    ResolveState getResolveState();

    /**
     * Returns the current version of the domain object.
     */
    Version getVersion();

    /**
     * Sets the versions of the domain object.
     */
    void setOptimisticLock(Version version);

    void fireChangedEvent();

    

    /**
     * Whether this instance belongs to another object (meaning its {@link #getOid()} will
     * be <tt>AggregatedOid</tt>.
     */
    boolean isAggregated();

}
// Copyright (c) Naked Objects Group Ltd.
