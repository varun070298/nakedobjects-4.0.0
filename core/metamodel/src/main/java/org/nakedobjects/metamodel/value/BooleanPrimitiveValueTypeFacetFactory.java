package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;


public class BooleanPrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public BooleanPrimitiveValueTypeFacetFactory() {
        super(BooleanValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != BooleanPrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new BooleanPrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
