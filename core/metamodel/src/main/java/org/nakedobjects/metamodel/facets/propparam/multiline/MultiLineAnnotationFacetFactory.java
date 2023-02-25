package org.nakedobjects.metamodel.facets.propparam.multiline;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.MultiLine;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class MultiLineAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public MultiLineAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    /**
     * In readiness for supporting <tt>@Value</tt> in the future.
     */
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final MultiLine annotation = getAnnotation(cls, MultiLine.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Class<?> returnType = method.getReturnType();
        if (!isString(returnType)) {
            return false;
        }
        final MultiLine annotation = getAnnotation(method, MultiLine.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (paramNum >= parameterTypes.length) {
            // ignore
            return false;
        }
        if (!isString(parameterTypes[paramNum])) {
            return false;
        }
        final Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof MultiLine) {
                final MultiLine annotation = (MultiLine) parameterAnnotations[j];
                return FacetUtil.addFacet(create(annotation, holder));
            }
        }
        return false;
    }

    private MultiLineFacet create(final MultiLine annotation, final FacetHolder holder) {
        return (annotation != null) ? new MultiLineFacetAnnotation(annotation.numberOfLines(), annotation.preventWrapping(),
                holder) : null;
    }

}
