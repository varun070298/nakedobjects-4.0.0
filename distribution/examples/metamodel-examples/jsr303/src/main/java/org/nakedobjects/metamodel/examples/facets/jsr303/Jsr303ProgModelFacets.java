package org.nakedobjects.metamodel.examples.facets.jsr303;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacetsJava5;


/**
 * Implementation of {@link ProgrammingModelFacets} that additionally just installs support for
 * {@link Jsr303PropertyValidationFacet JSR-303} validation.
 * 
 * <p>
 * This implementation is really provided only as an example. Typically you would provide your own
 * {@link ProgrammingModelFacets} implementation that installs other additional {@link FacetFactory facet
 * factories} ass required for your programming model.
 */
public class Jsr303ProgModelFacets extends ProgrammingModelFacetsJava5 {

	
	public Jsr303ProgModelFacets() {
		addFactory(Jsr303FacetFactory.class);
	}

}
