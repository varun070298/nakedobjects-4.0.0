package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.Identified;


/**
 * Responsible for managing the {@link NakedObject adapter}s and {@link Oid identities} for each and every
 * POJO that is being used by the framework.
 * 
 * <p>
 * It provides a consistent set of adapters in memory, providing an {@link NakedObject adapter} for the POJOs
 * that are in use ensuring that the same object is not loaded twice into memory.
 * 
 * <p>
 * Each POJO is given an {@link NakedObject adapter} so that the framework can work with the POJOs even though
 * it does not understand their types. Each POJO maps to an {@link NakedObject adapter} and these are reused.
 */
public interface AdapterManager extends AdapterManagerLookup, Injectable {

    
    
    ///////////////////////////////////////////////////////////
    // lookup/creation
    ///////////////////////////////////////////////////////////

    /**
     * Either returns an existing adapter (as per {@link #getAdapterFor(Object)}), otherwise creates either a
     * transient, standalone or aggregated {@link NakedObject adapter} for the supplied domain object,
     * depending on its {@link NakedObjectSpecification} and the context arguments provided.
     * 
     * <p>
     * If no adapter is found for the provided pojo, then the rules for creating the {@link NakedObject
     * adapter} are as follows:
     * <ul>
     * <li>if the pojo's {@link NakedObjectSpecification specification} indicates that this is an immutable
     * value, then a {@link ResolveState#VALUE} {@link NakedObject adapter} is created
     * <li>otherwise, if context <tt>ownerAdapter</tt> and <tt>identified</tt> arguments have both been
     * provided and also either the {@link Identified} argument indicates that for this particular
     * property/collection the object is aggregated <i>or</i> that the pojo's own
     * {@link NakedObjectSpecification specification} indicates that the pojo is intrinsically aggregated,
     * then an {@link NakedObject#isAggregated() aggregated} adapter is created. Note that the
     * {@link ResolveState} of such {@link NakedObject's} is independent of its <tt>ownerAdapter</tt>, but it
     * has the same {@link NakedObject#setOptimisticLock(Version) optimistic locking version}.
     * <li>otherwise, a {@link ResolveState#TRANSIENT} {@link NakedObject adapter} is created.
     * </ul>
     * 
     * @param pojo
     *            - pojo to adapt
     * @param ownerAdapter
     *            - only used if aggregated
     * @param identifier
     *            - only used if aggregated
     */
    public NakedObject adapterFor(final Object pojo, final NakedObject ownerAdapter, Identified identified);

    /**
     * Either returns an existing adapter (as per {@link #getAdapterFor(Object)}), otherwise creates either a
     * transient root or a standalone {@link NakedObject adapter} for the supplied domain object, depending on
     * its {@link NakedObjectSpecification}.
     * 
     * <p>
     * The rules for creating a {@link ResolveState#VALUE standalone} vs {@link ResolveState#TRANSIENT
     * transient} root {@link NakedObject adapter} are as for
     * {@link #adapterFor(Object, NakedObject, Identified)}.
     * 
     * <p>
     * Historical notes: previously called <tt>createAdapterForTransient</tt>, though this name wasn't quite
     * right.
     */
    NakedObject adapterFor(Object pojo);


    ///////////////////////////////////////////////////////////
    // removal
    ///////////////////////////////////////////////////////////

    /**
     * Removes the specified {@link NakedObject adapter} from the identity maps.
     */
    void removeAdapter(NakedObject adapter);

    /**
     * Removes the {@link NakedObject adapter} identified by the specified {@link Oid}.
     * 
     * <p>
     * Should be same as {@link #getAdapterFor(Oid)} followed by {@link #removeAdapter(NakedObject)}.
     */
	void removeAdapter(Oid oid);

}

// Copyright (c) Naked Objects Group Ltd.
