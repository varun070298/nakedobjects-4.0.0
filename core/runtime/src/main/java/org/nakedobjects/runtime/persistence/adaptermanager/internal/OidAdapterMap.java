package org.nakedobjects.runtime.persistence.adaptermanager.internal;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.component.Resettable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;


/**
 * A map of the objects' identities and the adapters' of the objects.
 */
public interface OidAdapterMap extends DebugInfo, Iterable<Oid>, SessionScopedComponent, Resettable {

    /**
     * Add an adapter for a given oid
     */
    public void add(final Oid oid, final NakedObject adapter);

    /**
     * Remove the adapter for the given oid
     * 
     * @return <tt>true</tt> if an adapter was removed.
     */
    public boolean remove(final Oid oid);

    /**
     * Get the adapter identified by the specified OID.
     */
    public NakedObject getAdapter(final Oid oid);

}
// Copyright (c) Naked Objects Group Ltd.
