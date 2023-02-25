package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.MoneyValueFacet;


public class MoneyValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public MoneyValueTypeFacetFactory() {
        super(MoneyValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.Money.class) {
            return false;
        }
        addFacets(new MoneyValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
