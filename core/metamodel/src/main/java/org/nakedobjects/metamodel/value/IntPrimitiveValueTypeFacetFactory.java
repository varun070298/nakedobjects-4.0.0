package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.IntegerValueFacet;


public class IntPrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public IntPrimitiveValueTypeFacetFactory() {
        super(IntegerValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != IntPrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new IntPrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
