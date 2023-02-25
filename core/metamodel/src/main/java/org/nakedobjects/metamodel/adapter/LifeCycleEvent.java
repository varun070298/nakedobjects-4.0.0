package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacet;


public class LifeCycleEvent {
    /**
     * Index for the life cycle method marking the logical creation of an object.
     */
    public static final LifeCycleEvent CREATED = new LifeCycleEvent(CreatedCallbackFacet.class);
    /**
     * Index for the life cycle method marking the end of the deleting process.
     */
    public static final LifeCycleEvent DELETED = new LifeCycleEvent(RemovedCallbackFacet.class);
    /**
     * Index for the life cycle method marking the beginning of the deleting process.
     */
    public static final LifeCycleEvent DELETING = new LifeCycleEvent(RemovingCallbackFacet.class);

    /**
     * Index for the life cycle method marking the end of the loading process.
     */
    public static final LifeCycleEvent LOADED = new LifeCycleEvent(LoadedCallbackFacet.class);

    /**
     * Index for the life cycle method marking the beginning of the loading process.
     */
    public static final LifeCycleEvent LOADING = new LifeCycleEvent(LoadingCallbackFacet.class);

    /**
     * Index for the life cycle method marking the end of the save process.
     */
    public static final LifeCycleEvent SAVED = new LifeCycleEvent(PersistedCallbackFacet.class);

    /**
     * Index for the life cycle method marking the beginning of the save process.
     */
    public static final LifeCycleEvent SAVING = new LifeCycleEvent(PersistingCallbackFacet.class);

    /**
     * Index for the life cycle method marking the end of the updating process.
     */
    public static final LifeCycleEvent UPDATED = new LifeCycleEvent(UpdatedCallbackFacet.class);

    /**
     * Index for the life cycle method marking the beginning of the updating process.
     */
    public static final LifeCycleEvent UPDATING = new LifeCycleEvent(UpdatingCallbackFacet.class);

    private final Class<? extends Facet> cls;

    private LifeCycleEvent(final Class<? extends Facet> cls) {
        this.cls = cls;
    }

    public Class<? extends Facet> getFacetClass() {
        return cls;
    }

}

// Copyright (c) Naked Objects Group Ltd.
