package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class OptionalAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public OptionalAnnotationFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            return false;
        }
        if (!isAnnotationPresent(method, Optional.class)) {
            return false;
        }
        final Optional annotation = getAnnotation(method, Optional.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (paramNum >= parameterTypes.length) {
            // ignore
            return false;
        }
        if (parameterTypes[paramNum].isPrimitive()) {
            return false;
        }
        final Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof Optional) {
                return FacetUtil.addFacet(new OptionalFacet(holder));
            }
        }
        return false;
    }

    private MandatoryFacet create(final Optional annotation, final FacetHolder holder) {
        return annotation != null ? new OptionalFacet(holder) : null;
    }

}
