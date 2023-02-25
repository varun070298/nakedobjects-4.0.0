package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;


public class TimeStampValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public TimeStampValueTypeFacetFactory() {
        super(DateValueFacet.class); // as per inherited DateTimeValueSemanticsProvider#facetType
        // (inherited)
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.TimeStamp.class) {
            return false;
        }
        addFacets(new TimeStampValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
