package org.nakedobjects.metamodel.adapter;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacet;


public class LifeCycleEventTest extends TestCase {

    private LifeCycleEvent lifeCycleEvent;

    public void testGetFacetClassforCreated() {
        lifeCycleEvent = LifeCycleEvent.CREATED;
        assertEquals(CreatedCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforDeleted() {
        lifeCycleEvent = LifeCycleEvent.DELETED;
        assertEquals(RemovedCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforDeleting() {
        lifeCycleEvent = LifeCycleEvent.DELETING;
        assertEquals(RemovingCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforLoaded() {
        lifeCycleEvent = LifeCycleEvent.LOADED;
        assertEquals(LoadedCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforLoading() {
        lifeCycleEvent = LifeCycleEvent.LOADING;
        assertEquals(LoadingCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforSaved() {
        lifeCycleEvent = LifeCycleEvent.SAVED;
        assertEquals(PersistedCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforSaving() {
        lifeCycleEvent = LifeCycleEvent.SAVING;
        assertEquals(PersistingCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforUpdated() {
        lifeCycleEvent = LifeCycleEvent.UPDATED;
        assertEquals(UpdatedCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

    public void testGetFacetClassforUpdating() {
        lifeCycleEvent = LifeCycleEvent.UPDATING;
        assertEquals(UpdatingCallbackFacet.class, lifeCycleEvent.getFacetClass());
    }

}

// Copyright (c) Naked Objects Group Ltd.
