package org.nakedobjects.metamodel.facetdecorator;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class FacetDecoratorAbstract implements FacetDecorator {
    
	public String getFacetTypeNames() {
		Class<? extends Facet>[] decoratorFacetTypes = getFacetTypes();
		StringBuilder buf = new StringBuilder();
        for (int i = 0; i < decoratorFacetTypes.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(decoratorFacetTypes[i].getName());
        }
        return buf.toString();
	}

	protected Facet replaceFacetWithDecoratingFacet(
			final Facet facet, Facet decoratingFacet, FacetHolder requiredHolder) {
		
		// we don't remove, so that the original facet points back to its facet holder
		// (eg a runtime peer object);
		// however, adding the decorating facet means that the required holder points to the 
		// decorating facet rather than the original facet
		
		// holder.removeFacet(facet);
		
		requiredHolder.addFacet(decoratingFacet);
		return decoratingFacet;
	}

}

// Copyright (c) Naked Objects Group Ltd.
