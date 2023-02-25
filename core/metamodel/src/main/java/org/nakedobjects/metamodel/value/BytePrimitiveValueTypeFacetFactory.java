package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ByteValueFacet;


public class BytePrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public BytePrimitiveValueTypeFacetFactory() {
        super(ByteValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != BytePrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new BytePrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
