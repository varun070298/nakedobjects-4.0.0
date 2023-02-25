package org.nakedobjects.metamodel.consent;

import org.nakedobjects.metamodel.facets.Facet;


/**
 * An instance of this type is used to allow something.
 */
public class Allow extends ConsentAbstract {

    private static final long serialVersionUID = 1L;

    public static Allow DEFAULT = new Allow();

    private Allow() {
        this((String) null);
    }

    /**
     * Called by DnD viewer; we should instead find a way to put the calling logic into {@link Facet}s so that
     * it is available for use by other viewers.
     * 
     * @see Veto
     * @deprecated
     * @param reasonVeteod
     * @param advisorClass
     */
    public Allow(final String description) {
        super(description, null);
    }

    public Allow(final InteractionResult interactionResult) {
        super(interactionResult);
    }

}
// Copyright (c) Naked Objects Group Ltd.
