package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.FloatingPointValueFacet;


public class PercentageValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public PercentageValueTypeFacetFactory() {
        super(FloatingPointValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.Percentage.class) {
            return false;
        }
        addFacets(new PercentageValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
