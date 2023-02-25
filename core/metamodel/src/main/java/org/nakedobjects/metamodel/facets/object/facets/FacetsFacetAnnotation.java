package org.nakedobjects.metamodel.facets.object.facets;

import org.nakedobjects.applib.annotation.Facets;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class FacetsFacetAnnotation extends FacetsFacetAbstract {

    public FacetsFacetAnnotation(final Facets annotation, final FacetHolder holder) {
        super(annotation.facetFactoryNames(), annotation.facetFactoryClasses(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
