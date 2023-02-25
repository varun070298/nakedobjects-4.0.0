package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DoubleFloatingPointValueFacet;


public class DoublePrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public DoublePrimitiveValueTypeFacetFactory() {
        super(DoubleFloatingPointValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != DoublePrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new DoublePrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
