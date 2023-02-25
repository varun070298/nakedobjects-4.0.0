package org.nakedobjects.metamodel.facets.propparam.typicallength;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class TypicalLengthAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public TypicalLengthAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final TypicalLength annotation = getAnnotation(cls, TypicalLength.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final TypicalLength annotation = getAnnotation(method, TypicalLength.class);
        final TypicalLengthFacet facet = create(annotation, holder);

        return FacetUtil.addFacet(facet);
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof TypicalLength) {
                final TypicalLength annotation = (TypicalLength) parameterAnnotations[j];
                return FacetUtil.addFacet(create(annotation, holder));
            }
        }
        return false;
    }

    private TypicalLengthFacet create(final TypicalLength annotation, final FacetHolder holder) {
        return annotation != null ? new TypicalLengthFacetAnnotation(annotation.value(), holder) : null;
    }

}
