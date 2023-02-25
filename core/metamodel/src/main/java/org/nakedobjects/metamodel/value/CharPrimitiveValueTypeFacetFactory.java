package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.CharValueFacet;


public class CharPrimitiveValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public CharPrimitiveValueTypeFacetFactory() {
        super(CharValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != CharPrimitiveValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new CharPrimitiveValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
