package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.java5.FallbackFacetFactory;


/**
 * Installed by the {@link FallbackFacetFactory}, and means that this class <i>is</i> persistable (ie not
 * {@link NotPersistableFacet not persistable}).
 */
public class NotPersistableFacetNull extends NotPersistableFacetAbstract {

    public NotPersistableFacetNull(final FacetHolder holder) {
        super(null, holder);
    }

    /**
     * Always returns <tt>null</tt> (that is, does <i>not</i> disable).
     */
    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        return null;
    }

}

// Copyright (c) Naked Objects Group Ltd.
