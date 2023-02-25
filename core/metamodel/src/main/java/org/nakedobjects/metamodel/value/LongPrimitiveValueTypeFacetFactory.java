package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.LongValueFacet;


public class LongPrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public LongPrimitiveValueTypeFacetFactory() {
        super(LongValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != LongPrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new LongPrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
