package org.nakedobjects.metamodel.facets.ordering.fieldorder;

import org.nakedobjects.applib.annotation.FieldOrder;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class FieldOrderAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public FieldOrderAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final FieldOrder annotation = getAnnotation(cls, FieldOrder.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private FieldOrderFacet create(final FieldOrder annotation, final FacetHolder holder) {
        return annotation == null ? null : new FieldOrderFacetAnnotation(annotation.value(), holder);
    }

}
