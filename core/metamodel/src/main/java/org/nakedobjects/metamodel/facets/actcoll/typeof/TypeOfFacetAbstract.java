package org.nakedobjects.metamodel.facets.actcoll.typeof;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleClassValueFacetAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class TypeOfFacetAbstract extends SingleClassValueFacetAbstract implements TypeOfFacet {

    public static Class<? extends Facet> type() {
        return TypeOfFacet.class;
    }

    public TypeOfFacetAbstract(
            final Class<?> type,
            final FacetHolder holder,
            final SpecificationLoader specificationLoader) {
        super(type(), holder, type, specificationLoader);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + value() + "]";
    }
}
