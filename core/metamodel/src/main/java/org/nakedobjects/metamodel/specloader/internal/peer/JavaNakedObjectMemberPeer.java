package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;


public abstract class JavaNakedObjectMemberPeer extends FacetHolderImpl implements NakedObjectMemberPeer {

    private final Identifier identifier;

    public JavaNakedObjectMemberPeer(final Identifier identifier) {
        this.identifier = identifier;
    }

    public void debugData(final DebugString debug) {
    // debug.appendln("Identifier", identifier.toString());
    }

    public Identifier getIdentifier() {
        return identifier;
    }

}
// Copyright (c) Naked Objects Group Ltd.
