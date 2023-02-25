package org.nakedobjects.metamodel.facets.naming.named;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NamedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public NamedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.EVERYTHING);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Named annotation = getAnnotation(cls, Named.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Named annotation = getAnnotation(method, Named.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof Named) {
                final Named annotation = (Named) parameterAnnotations[j];
                return FacetUtil.addFacet(create(annotation, holder));
            }
        }
        return false;
    }

    private NamedFacet create(final Named annotation, final FacetHolder holder) {
        return annotation != null ? new NamedFacetAnnotation(annotation.value(), holder) : null;
    }

}
