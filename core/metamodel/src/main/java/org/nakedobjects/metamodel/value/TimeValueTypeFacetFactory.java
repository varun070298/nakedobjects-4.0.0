package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;


public class TimeValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public TimeValueTypeFacetFactory() {
        super(DateValueFacet.class); // as per inherited DateTimeValueSemanticsProvider#facetType
        // (inherited)
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.Time.class) {
            return false;
        }
        addFacets(new TimeValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
