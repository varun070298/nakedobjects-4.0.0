package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.CharValueFacet;


public class CharWrapperValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public CharWrapperValueTypeFacetFactory() {
        super(CharValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != CharWrapperValueSemanticsProvider.adaptedClass()) {
            return false;
        }
        addFacets(new CharWrapperValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
