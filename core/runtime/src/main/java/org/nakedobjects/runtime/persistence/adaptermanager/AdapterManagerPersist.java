package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;


/**
 * API used solely by the {@link PersistenceSession}.
 */
public interface AdapterManagerPersist {

    /**
     * Remaps the {@link NakedObject adapter} and any associated aggregated (collection) adapters using a 
     * new value for the {@link Oid} provided by the {@link OidGenerator}. 
     * 
     * <p>
     * The {@link Oid} of the supplied {@link NakedObject adapter} should be in such a state that it can be
     * {@link OidGenerator#convertTransientToPersistentOid(Oid) converted from transient to persistent}. Note
     * that some {@link Oid} implementations require an initial state call to do this (eg to read from a
     * database identity or sequence value).
     * 
     * <p>
     * The adapter is remapped in the {@link AdapterManager}, and the {@link Oid#getPrevious() previous} is
     * set to its transient value).  Similarly for any aggregated adapters.  This is needed for client/server so that the client can remap a changed object.
     * 
     * @see AdapterManagerProxy#remapUpdated(Oid)
     */
    void remapAsPersistent(NakedObject adapter);

    /**
     * Either returns an existing {@link NakedObject adapter} (as per {@link #getAdapterFor(Object)} or
     * {@link #getAdapterFor(Oid)}), otherwise re-creates an adapter with the specified (persistent)
     * {@link Oid}.
     * 
     * <p>
     * Typically called when the {@link Oid} is already known, that is, when resolving an already-persisted
     * object. Is also available for <tt>Memento</tt> support however, so {@link Oid} could also represent a
     * {@link Oid#isTransient() transient} object.
     * 
     * <p>
     * If the {@link NakedObject adapter} is recreated, its {@link ResolveState} will be
     * {@link ResolveState#GHOST} if a persistent {@link Oid}, or {@link ResolveState#TRANSIENT} otherwise.
     */
    NakedObject recreateRootAdapter(Oid oid, Object pojo);

}

// Copyright (c) Naked Objects Group Ltd.
