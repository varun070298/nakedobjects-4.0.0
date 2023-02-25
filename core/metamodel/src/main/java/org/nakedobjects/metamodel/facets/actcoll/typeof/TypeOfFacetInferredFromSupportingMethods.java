package org.nakedobjects.metamodel.facets.actcoll.typeof;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class TypeOfFacetInferredFromSupportingMethods extends TypeOfFacetAbstract {

    public TypeOfFacetInferredFromSupportingMethods(
            final Class<?> type,
            final FacetHolder holder,
            final SpecificationLoader specificationLoader) {
        super(type, holder, specificationLoader);
    }

}

// Copyright (c) Naked Objects Group Ltd.
