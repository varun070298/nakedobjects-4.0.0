package org.nakedobjects.metamodel.value;

import java.util.Date;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;


public class JavaUtilDateValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaUtilDateValueTypeFacetFactory() {
        super(DateValueFacet.class); // as per inherited TimeValueSemanticsProvider#facetType (inherited)
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != Date.class) {
            return false;
        }
        addFacets(new JavaUtilDateValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
