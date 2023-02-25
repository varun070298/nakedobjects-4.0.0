package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.LongValueFacet;


public class LongWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public LongWrapperValueTypeFacetFactory() {
        super(LongValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != LongWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new LongWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
