package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.FloatingPointValueFacet;


public class FloatPrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public FloatPrimitiveValueTypeFacetFactory() {
        super(FloatingPointValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != FloatPrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new FloatPrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
