package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.value.Color;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ColorValueFacet;


public class ColorValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public ColorValueTypeFacetFactory() {
        super(ColorValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != Color.class) {
            return false;
        }
        addFacets(new ColorValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
