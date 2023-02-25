package org.nakedobjects.metamodel.specloader.progmodelfacets;

import java.util.List;

import org.nakedobjects.metamodel.facets.FacetFactory;


public interface ProgrammingModelFacets {

    void init();

    List<FacetFactory> getList();

	void addFactory(Class<? extends FacetFactory> facetFactoryClass);
	
	void removeFactory(Class <? extends FacetFactory> facetFactoryClass);
}
