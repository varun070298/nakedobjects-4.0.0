package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ImageValueFacet;


public class JavaAwtImageValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaAwtImageValueTypeFacetFactory() {
        super(ImageValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != java.awt.Image.class) {
            return false;
        }
        addFacets(new JavaAwtImageValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
