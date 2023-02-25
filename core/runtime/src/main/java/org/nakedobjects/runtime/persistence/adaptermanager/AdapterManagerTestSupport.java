package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;

public interface AdapterManagerTestSupport {


    /**
     * For testing purposes, creates an {@link NakedObject adapter} for the supplied domain object 
     * with the specified {@link Oid}.
     * 
     * <p>
     * The usual way of creating {@link NakedObject adapter}s is using {@link #adapterFor(Object)}, using
     * the <tt>OidGenerator</tt> to obtain an {@link Oid}.  This test-support method differs because it allows 
     * the {@link Oid} to be specified explicitly. 
     *  
     * <p>
     * Note that the {@link Oid} must represent a {@link Oid#isTransient() transient} object.  If an
     * {@link NakedObject adapter} is required for a persistent {@link Oid}, just use {@link #recreateRootAdapter(Object, Oid)}.
     * 
     * @see #adapterFor(Object)
     * @see #recreateRootAdapter(Object, Oid) 
     */
    NakedObject testCreateTransient(Object pojo, Oid oid);

}


// Copyright (c) Naked Objects Group Ltd.
