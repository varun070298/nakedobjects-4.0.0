package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;


public class JavaSqlTimeStampValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaSqlTimeStampValueTypeFacetFactory() {
        super(DateValueFacet.class); // as per inherited DateTimeValueSemanticsProvider#facetType
        // (inherited)
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != java.sql.Timestamp.class) {
            return false;
        }
        addFacets(new JavaSqlTimeStampValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
