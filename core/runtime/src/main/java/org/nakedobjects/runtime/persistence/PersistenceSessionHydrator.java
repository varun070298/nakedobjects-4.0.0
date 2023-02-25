package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;


public interface PersistenceSessionHydrator extends Injectable {

    /**
     * Returns an {@link NakedObject adapter} of the {@link NakedObjectSpecification type} specified.
     * 
     * <p>
     * If an adapter exists in the {@link AdapterManager map} then that adapter is returned immediately.
     * Otherwise a new domain object of the type specified is {@link ObjectFactory created} and then an
     * adapter is recreated as per {@link #recreateAdapter(Oid, Object)}.
     * 
     * <p>
     * Note: the similar looking method
     * {@link PersistenceSessionContainer#loadObject(Oid, NakedObjectSpecification)} retrieves the existing
     * object from the persistent store (if not available in the {@link AdapterManager maps} . Once the object
     * has been retrieved, the object store calls back to {@link #recreateAdapter(Oid, Object)} to map it.
     * 
     * @see #recreateAdapter(Oid, Object)
     * @see PersistenceSessionContainer#loadObject(Oid, NakedObjectSpecification)
     */
    NakedObject recreateAdapter(Oid oid, NakedObjectSpecification specification);

    /**
     * Returns an adapter for the provided {@link Oid}, wrapping the provided domain object.
     * 
     * <p>
     * If an adapter exists in the {@link AdapterManager map} for either the {@link Oid} or the domain object
     * then that adapter is returned immediately. Otherwise a new adapter is created using the specified
     * {@link Oid} and its resolved state set to either {@link ResolveState#TRANSIENT} or
     * {@link ResolveState#GHOST} based on whether the {@link Oid} is {@link Oid#isTransient() transient} or
     * not.
     */
    NakedObject recreateAdapter(Oid oid, Object pojo);
}

// Copyright (c) Naked Objects Group Ltd.
