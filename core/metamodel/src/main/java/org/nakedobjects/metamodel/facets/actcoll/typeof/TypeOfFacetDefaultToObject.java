package org.nakedobjects.metamodel.facets.actcoll.typeof;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class TypeOfFacetDefaultToObject extends TypeOfFacetAbstract {

    public TypeOfFacetDefaultToObject(
            final FacetHolder holder, 
            final SpecificationLoader specificationLoader) {
        super(Object.class, holder, specificationLoader);
    }
    

}

// Copyright (c) Naked Objects Group Ltd.
