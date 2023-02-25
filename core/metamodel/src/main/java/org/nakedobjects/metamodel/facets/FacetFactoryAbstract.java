package org.nakedobjects.metamodel.facets;


import java.lang.reflect.Method;

import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;


public abstract class FacetFactoryAbstract implements FacetFactory, SpecificationLoaderAware {

    private final NakedObjectFeatureType[] featureTypes;
    
    private SpecificationLoader specificationLoader;

    public FacetFactoryAbstract(final NakedObjectFeatureType[] featureTypes) {
        this.featureTypes = featureTypes;
    }

    public NakedObjectFeatureType[] getFeatureTypes() {
        return featureTypes;
    }

    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return false;
    }

    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        return false;
    }

    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        return false;
    }

    
    //////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    //////////////////////////////////////////////////////////////////
    
    protected SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

    /**
     * Injected
     */
    public void setSpecificationLoader(final SpecificationLoader specificationLoader) {
        this.specificationLoader = specificationLoader;
    }

}
