package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DoubleFloatingPointValueFacet;


public class DoubleWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public DoubleWrapperValueTypeFacetFactory() {
        super(DoubleFloatingPointValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != DoubleWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new DoubleWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
