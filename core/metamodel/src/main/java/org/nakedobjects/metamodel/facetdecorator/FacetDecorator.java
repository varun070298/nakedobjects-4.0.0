package org.nakedobjects.metamodel.facetdecorator;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public interface FacetDecorator {

	/**
	 * If applicable, replace the provided {@link Facet} with a decorating {@link Facet} that should
	 * be held by the provided {@link FacetHolder required holder}.
	 * 
	 * <p>
	 * @return the decorating {@link Facet}, or <tt>null</tt> if this decoration does not apply to this Facet.
	 */
    Facet decorate(Facet facet, FacetHolder requiredHolder);

    Class<? extends Facet>[] getFacetTypes();
    
	String getFacetTypeNames();


}

// Copyright (c) Naked Objects Group Ltd.
