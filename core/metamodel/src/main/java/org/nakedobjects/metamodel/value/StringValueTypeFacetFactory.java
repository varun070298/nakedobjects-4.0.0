package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.StringValueFacet;


public class StringValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public StringValueTypeFacetFactory() {
        super(StringValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != String.class) {
            return false;
        }
        addFacets(new StringValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
