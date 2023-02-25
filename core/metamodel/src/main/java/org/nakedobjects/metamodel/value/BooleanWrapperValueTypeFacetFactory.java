package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;


public class BooleanWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public BooleanWrapperValueTypeFacetFactory() {
        super(BooleanValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != BooleanWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new BooleanWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
