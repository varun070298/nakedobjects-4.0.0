package org.nakedobjects.metamodel.facets.object.bounded;

import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class BoundedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public BoundedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Bounded annotation = (Bounded) getAnnotation(cls, Bounded.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private BoundedFacet create(final Bounded annotation, final FacetHolder holder) {
        return annotation == null ? null : new BoundedFacetAnnotation(holder);
    }

}
