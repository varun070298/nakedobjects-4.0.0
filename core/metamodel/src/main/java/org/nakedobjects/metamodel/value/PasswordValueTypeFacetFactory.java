package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.PasswordValueFacet;


public class PasswordValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public PasswordValueTypeFacetFactory() {
        super(PasswordValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.Password.class) {
            return false;
        }
        addFacets(new PasswordValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
