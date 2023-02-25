package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.specloader.ReflectiveActionException;


public abstract class NakedObjectActionPeerDecorator implements NakedObjectActionPeer {
    private final NakedObjectActionPeer decorated;

    public NakedObjectActionPeerDecorator(final NakedObjectActionPeer decorated) {
        this.decorated = decorated;
    }

    public void debugData(final DebugString debugString) {
        decorated.debugData(debugString);
    }

    public NakedObject execute(final NakedObject object, final NakedObject[] parameters) throws ReflectiveActionException {
        return decorated.execute(object, parameters);
    }

    public <T extends Facet> T getFacet(final Class<T> cls) {
        return decorated.getFacet(cls);
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return decorated.getFacetTypes();
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return decorated.getFacets(filter);
    }

    public Identifier getIdentifier() {
        return decorated.getIdentifier();
    }

    public NakedObjectActionParamPeer[] getParameters() {
        return decorated.getParameters();
    }

}
// Copyright (c) Naked Objects Group Ltd.
