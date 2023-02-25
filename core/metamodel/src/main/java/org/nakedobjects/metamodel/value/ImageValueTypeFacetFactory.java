package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.ImageValueFacet;


public class ImageValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public ImageValueTypeFacetFactory() {
        super(ImageValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != org.nakedobjects.applib.value.Image.class) {
            return false;
        }
        addFacets(new ImageValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }

}
