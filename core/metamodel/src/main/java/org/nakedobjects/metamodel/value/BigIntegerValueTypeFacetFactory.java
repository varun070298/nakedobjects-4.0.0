package org.nakedobjects.metamodel.value;

import java.math.BigInteger;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.BigIntegerValueFacet;


public class BigIntegerValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public BigIntegerValueTypeFacetFactory() {
        super(BigIntegerValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != BigInteger.class) {
            return false;
        }
        addFacets(new BigIntegerValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
