package org.nakedobjects.runtime.help;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.help.HelpFacet;
import org.nakedobjects.metamodel.spec.identifier.Identified;


public class StandardHelpFacetDecorator  extends FacetDecoratorAbstract implements HelpFacetDecorator {
    private final HelpManager helpManager;

    public StandardHelpFacetDecorator(final HelpManager manager) {
        helpManager = manager;
    }

    public Facet decorate(final Facet facet, FacetHolder requiredHolder) {
    	if (!(requiredHolder instanceof Identified)) {
    		return null;
    	} 

    	Identified identified = (Identified) requiredHolder;
		final Identifier identifier = identified.getIdentifier();
		
        if (facet.facetType() == HelpFacet.class) {
            final String lookupHelp = helpManager.help(identifier);
            if (lookupHelp != null) {
                HelpFacetLookup decoratingFacet = new HelpFacetLookup(lookupHelp, facet.getFacetHolder());
				return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
            }
        }

        return facet;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[] { HelpFacet.class };
    }
}

// Copyright (c) Naked Objects Group Ltd.
