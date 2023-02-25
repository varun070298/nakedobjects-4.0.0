package org.nakedobjects.metamodel.value;

import java.math.BigDecimal;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.BigDecimalValueFacet;


public class BigDecimalValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {


	public BigDecimalValueTypeFacetFactory() {
        super(BigDecimalValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != BigDecimal.class) {
            return false;
        }
        addFacets(new BigDecimalValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }


}
