package org.nakedobjects.examples.reflect.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.help.HelpFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.spec.identifier.Identified;


public class CapitalisedNameFacetDecorator extends FacetDecoratorAbstract {

    public Facet decorate(final Facet facet, FacetHolder requiredHolder) {
    	if (!(requiredHolder instanceof Identified)) {
        	return null;
		} 
        Identified identified = (Identified) requiredHolder;
		final Identifier identifier = identified.getIdentifier();
		if (facet.facetType() == NamedFacet.class) {
		    String name = identifier.getMemberNaturalName().toUpperCase();
		    CapitalisedNameFacet decoratingFacet = new CapitalisedNameFacet(name, requiredHolder);
			return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
		}
        return facet;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[] { HelpFacet.class };
    }

}

// Copyright (c) Naked Objects Group Ltd.
