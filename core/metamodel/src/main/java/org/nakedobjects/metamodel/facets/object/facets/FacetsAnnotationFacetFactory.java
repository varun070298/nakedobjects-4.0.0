package org.nakedobjects.metamodel.facets.object.facets;

import org.nakedobjects.applib.annotation.Facets;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class FacetsAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public FacetsAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Facets annotation = (Facets) getAnnotation(cls, Facets.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    /**
     * Returns a {@link FacetsFacet} impl provided that at least one valid
     * {@link FacetsFacet#facetFactories() factory} was specified.
     */
    private FacetsFacet create(final Facets annotation, final FacetHolder holder) {
        if (annotation == null) {
            return null;
        }
        final FacetsFacetAnnotation facetsFacetAnnotation = new FacetsFacetAnnotation(annotation, holder);
        return facetsFacetAnnotation.facetFactories().length > 0 ? facetsFacetAnnotation : null;
    }

}
