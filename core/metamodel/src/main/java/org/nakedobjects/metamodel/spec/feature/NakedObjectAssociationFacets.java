package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.propcoll.notpersisted.NotPersistedFacet;


public class NakedObjectAssociationFacets {

    public static boolean isHidden(final NakedObjectAssociation association) {
        return association.getFacet(HiddenFacet.class) != null;
    }

    public static boolean isNotPersisted(final NakedObjectAssociation association) {
        return association.getFacet(NotPersistedFacet.class) != null;
    }

    private NakedObjectAssociationFacets() {}
}

// Copyright (c) Naked Objects Group Ltd.
