package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ShortValueFacet;


public class ShortWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public ShortWrapperValueTypeFacetFactory() {
        super(ShortValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != ShortWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new ShortWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
