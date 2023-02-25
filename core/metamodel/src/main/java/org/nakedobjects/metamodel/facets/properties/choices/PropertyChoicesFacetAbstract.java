package org.nakedobjects.metamodel.facets.properties.choices;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PropertyChoicesFacetAbstract extends FacetAbstract implements PropertyChoicesFacet {

    public static Class<? extends Facet> type() {
        return PropertyChoicesFacet.class;
    }

    public PropertyChoicesFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}
