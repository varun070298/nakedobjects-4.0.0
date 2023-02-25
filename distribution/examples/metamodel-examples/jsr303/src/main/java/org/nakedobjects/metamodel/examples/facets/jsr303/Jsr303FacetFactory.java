package org.nakedobjects.metamodel.examples.facets.jsr303;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class Jsr303FacetFactory implements FacetFactory {

    public NakedObjectFeatureType[] getFeatureTypes() {
        return NakedObjectFeatureType.OBJECTS_AND_PROPERTIES;
    }

    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return false;
    }

    /**
     * Simply attaches
     */
    public boolean process(final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(new Jsr303PropertyValidationFacet(holder));
    }

    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        // nothing to do
        return false;
    }
    
    
    /*
     * QualitaCorpus.class: we implemented the method below
     * because the developers may forget to implement it
     * since this class is not abstract and the interface
     * requires such method.
     */    
    public boolean process(Class<?> cls, Method method,
    		MethodRemover methodRemover, FacetHolder holder) {
    	// TODO Auto-generated method stub
    	return false;
    }

}
