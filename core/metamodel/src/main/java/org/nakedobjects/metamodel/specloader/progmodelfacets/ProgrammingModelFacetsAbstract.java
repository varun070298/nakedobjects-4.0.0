package org.nakedobjects.metamodel.specloader.progmodelfacets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.facets.FacetFactory;


public abstract class ProgrammingModelFacetsAbstract implements ProgrammingModelFacets {

    private final List<FacetFactory> facetFactories = new ArrayList<FacetFactory>();
    private final List<Class<? extends FacetFactory>> facetFactoryClasses = new ArrayList<Class<? extends FacetFactory>>();

    public final List<FacetFactory> getList() {
        return Collections.unmodifiableList(facetFactories);
    }

    public final void addFactory(final Class<? extends FacetFactory> factoryClass) {
        facetFactoryClasses.add(factoryClass);
    }

    public final void removeFactory(final Class<? extends FacetFactory> factoryClass) {
        facetFactoryClasses.remove(factoryClass);
    }


    public void init() {
    	for(Class<? extends FacetFactory> factoryClass: facetFactoryClasses) {
    		FacetFactory facetFactory = 
    			(FacetFactory) InstanceFactory.createInstance(factoryClass);
    		facetFactories.add(facetFactory);
    	}
    }

}
