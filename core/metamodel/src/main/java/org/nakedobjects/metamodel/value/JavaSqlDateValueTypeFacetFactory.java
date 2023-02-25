package org.nakedobjects.metamodel.value;

import java.sql.Date;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;


public class JavaSqlDateValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaSqlDateValueTypeFacetFactory() {
        super(DateValueFacet.class); // as per inherited TimeValueSemanticsProvider#facetType (inherited)
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != Date.class) {
            return false;
        }
        addFacets(new JavaSqlDateValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
