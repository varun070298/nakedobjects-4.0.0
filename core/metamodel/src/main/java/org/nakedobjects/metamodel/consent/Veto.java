package org.nakedobjects.metamodel.consent;

import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.nonEmptyString;

import org.nakedobjects.metamodel.facets.Facet;

public class Veto extends ConsentAbstract {

    private static final long serialVersionUID = 1L;

    public static Veto DEFAULT = new Veto("Vetoed by default");

    /**
     * Called by DnD viewer; we should instead find a way to put the calling logic into {@link Facet}s so that
     * it is available for use by other viewers.
     * 
     * @param reasonVeteod - must not be <tt>null</tt>
     */
    public Veto(final String reasonVetoed) {
        super(null, ensureThatArg(reasonVetoed, nonEmptyString()));
    }

    public Veto(final InteractionResult interactionResult) {
        super(interactionResult);
    }

}
// Copyright (c) Naked Objects Group Ltd.
