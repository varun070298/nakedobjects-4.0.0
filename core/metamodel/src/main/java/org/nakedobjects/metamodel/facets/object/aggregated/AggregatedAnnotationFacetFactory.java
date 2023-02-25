package org.nakedobjects.metamodel.facets.object.aggregated;

import org.nakedobjects.applib.annotation.Aggregated;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class AggregatedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public AggregatedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Aggregated annotation = (Aggregated) getAnnotation(cls, Aggregated.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private AggregatedFacet create(final Aggregated annotation, final FacetHolder holder) {
        return annotation == null ? null : new AggregatedFacetAnnotation(holder);
    }

}
