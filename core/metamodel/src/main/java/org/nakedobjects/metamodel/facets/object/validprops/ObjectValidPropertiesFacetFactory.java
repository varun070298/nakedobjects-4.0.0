package org.nakedobjects.metamodel.facets.object.validprops;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ObjectValidPropertiesFacetFactory extends FacetFactoryAbstract {

    public ObjectValidPropertiesFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        return FacetUtil.addFacet(new ObjectValidPropertiesFacetImpl(facetHolder));
    }
}
