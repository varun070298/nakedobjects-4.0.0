package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.component.Resettable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.runtime.persistence.PersistenceSession;


/**
 * Extension of the {@link AdapterManager} as viewed by the {@link PersistenceSession}.
 * 
 * <p>
 * Extends the {@link AdapterManager} interface in various ways, providing additional support:
 * <ul>
 * <li> for the {@link PersistenceSession} itself (by extending the {@link AdapterManagerPersist} interface),
 * <li> for tests (by extending {@link AdapterManagerTestSupport}) and,
 * <li> for slightly dodgy implementations (such as the <tt>MemoryObjectStore</tt> that manipulate the 
 *      identity maps directly (by extending {@link AdapterManagerBackdoor}).
 * </ul>
 */
public interface AdapterManagerExtended 
        extends Iterable<NakedObject>, 
                Resettable, 
                AdapterManager, 
                AdapterManagerPersist, 
                AdapterManagerLookup, 
                AdapterManagerProxy, 
                AdapterManagerTestSupport, 
                AdapterManagerBackdoor, 
                DebugInfo,
                SessionScopedComponent {

    
}

// Copyright (c) Naked Objects Group Ltd.
