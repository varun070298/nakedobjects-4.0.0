package org.nakedobjects.metamodel.facets.propparam.validate.maxlength;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class MaxLengthAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public MaxLengthAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    /**
     * In readiness for supporting <tt>@Value</tt> in the future.
     */
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final MaxLength annotation = getAnnotation(cls, MaxLength.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final MaxLength annotation = getAnnotation(method, MaxLength.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final java.lang.annotation.Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];

        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof MaxLength) {
                final MaxLength annotation = (MaxLength) parameterAnnotations[j];
                return FacetUtil.addFacet(create(annotation, holder));
            }
        }
        return false;
    }

    private MaxLengthFacet create(final MaxLength annotation, final FacetHolder holder) {
        return annotation == null ? null : new MaxLengthFacetAnnotation(annotation.value(), holder);
    }
}
