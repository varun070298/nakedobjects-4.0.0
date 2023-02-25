package org.nakedobjects.runtime.persistence.objectstore.algorithm;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.NotPersistableException;


public abstract class PersistAlgorithmAbstract implements PersistAlgorithm {

    

    //////////////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////////////

    public void init() {}

    public void shutdown() {}


    //////////////////////////////////////////////////////////////////
    // helpers
    //////////////////////////////////////////////////////////////////

    /**
     * Whether the persist algorithm should skip over this object.
     * 
     * <p>
     * There are various reasons why an object should not be persisted:
     * <ul>
     * <li>it is already persisted
     * <li>its {@link NakedObjectSpecification specification} indicates instances of its
     * type should not be persisted.
     * <li>it is {@link ResolveState#VALUE standalone}
     * <li>it is a {@link NakedObjectSpecification#isService() service}.
     * </ul>
     * 
     * <p>
     * Implementation note: the only reason that this method has not been combined with
     * the weaker check in {@link #alreadyPersistedOrNotPersistable(NakedObject)} is because of 
     * existing code that throws an exception if the latter is not fulfilled.  
     * <b><i>REVIEW: should try to combine and remove the other method</i></b>. 
     */
    protected boolean alreadyPersistedOrNotPersistableOrServiceOrStandalone(final NakedObject adapter) {
        return objectIsStandalone(adapter) || 
               objectSpecIsService(adapter) ||
               alreadyPersistedOrNotPersistable(adapter); 
    }

    /**
     * If has a {@link ResolveState} that is already persisted or has a
     * {@link NakedObjectSpecification specification} that indicates instances of its
     * type should not be persisted.
     * 
     * @see #alreadyPersistedOrNotPersistableOrServiceOrStandalone(NakedObject)
     */
    protected boolean alreadyPersistedOrNotPersistable(final NakedObject adapter) {
        return adapter.isPersistent() || 
               objectSpecNotPersistable(adapter);
    }


    /**
     * As per {@link #alreadyPersistedOrNotPersistable(NakedObject)}, ensures object can be
     * persisted else throws {@link NotPersistableException}. 
     */
    protected void assertObjectNotPersistentAndPersistable(final NakedObject object) {
        if (alreadyPersistedOrNotPersistable(object)) {
            throw new NotPersistableException(
                    "can't make object persistent - either already persistent, " +
                    "or transient only: " + object);
        }
    }


    private boolean objectIsStandalone(final NakedObject adapter) {
        return adapter.getResolveState().isValue();
    }

    private boolean objectSpecNotPersistable(final NakedObject adapter) {
        return !adapter.getSpecification().persistability().isPersistable();
    }

    private boolean objectSpecIsService(final NakedObject adapter) {
        return adapter.getSpecification().isService();
    }


}
// Copyright (c) Naked Objects Group Ltd.
