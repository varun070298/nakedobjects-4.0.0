package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.FloatingPointValueFacet;


public class FloatWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public FloatWrapperValueTypeFacetFactory() {
        super(FloatingPointValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != FloatWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new FloatWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
