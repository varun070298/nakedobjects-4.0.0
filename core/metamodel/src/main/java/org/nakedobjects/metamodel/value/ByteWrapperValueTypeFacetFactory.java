package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ByteValueFacet;


public class ByteWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public ByteWrapperValueTypeFacetFactory() {
        super(ByteValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != ByteWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new ByteWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
