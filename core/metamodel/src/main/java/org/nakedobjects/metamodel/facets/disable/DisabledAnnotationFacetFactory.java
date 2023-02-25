package org.nakedobjects.metamodel.facets.disable;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DisabledAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public DisabledAnnotationFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_COLLECTIONS_AND_ACTIONS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Disabled annotation = getAnnotation(method, Disabled.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private DisabledFacet create(final Disabled annotation, final FacetHolder holder) {
        return annotation == null ? null : new DisabledFacetAnnotation(decodeWhen(annotation.value()), holder);
    }

}
