package org.nakedobjects.metamodel.facets.naming.describedas;

import org.nakedobjects.metamodel.facets.FacetHolder;


public class DescribedAsFacetDerivedFromType extends DescribedAsFacetAbstract {

    public DescribedAsFacetDerivedFromType(final DescribedAsFacet describedAsFacet, final FacetHolder holder) {
        super(describedAsFacet.value(), holder);
    }

}
