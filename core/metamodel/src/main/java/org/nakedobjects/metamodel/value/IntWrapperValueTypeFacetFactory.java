package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.IntegerValueFacet;


public class IntWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public IntWrapperValueTypeFacetFactory() {
        super(IntegerValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != IntWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new IntWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
