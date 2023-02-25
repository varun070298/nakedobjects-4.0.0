package org.nakedobjects.metamodel.facets.naming.describedas;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DescribedAsAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public DescribedAsAnnotationFacetFactory() {
        super(NakedObjectFeatureType.EVERYTHING);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final DescribedAs annotation = (DescribedAs) getAnnotation(cls, DescribedAs.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {

        // look for annotation on the property
        final DescribedAs annotation = getAnnotation(method, DescribedAs.class);
        DescribedAsFacet facet = create(annotation, holder);
        if (facet != null) {
            return FacetUtil.addFacet(facet);
        }

        // otherwise, look for annotation on the type
        final Class<?> returnType = method.getReturnType();
        final DescribedAsFacet returnTypeDescribedAsFacet = getDescribedAsFacet(returnType);
        if (returnTypeDescribedAsFacet != null) {
            facet = new DescribedAsFacetDerivedFromType(returnTypeDescribedAsFacet, holder);
            return FacetUtil.addFacet(facet);
        }
        return false;
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {

        final Class<?> parameterType = method.getParameterTypes()[paramNum];
        final Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof DescribedAs) {
                return FacetUtil.addFacet(create((DescribedAs) parameterAnnotations[j], holder));
            }
        }

        // otherwise, fall back to a description on the parameter'ss type, if available
        final DescribedAsFacet parameterTypeDescribedAsFacet = getDescribedAsFacet(parameterType);
        if (parameterTypeDescribedAsFacet != null) {
            return FacetUtil.addFacet(new DescribedAsFacetDerivedFromType(parameterTypeDescribedAsFacet, holder));
        }

        return false;
    }

    private DescribedAsFacet create(final DescribedAs annotation, final FacetHolder holder) {
        return annotation == null ? null : new DescribedAsFacetAnnotation(annotation.value(), holder);
    }

    private DescribedAsFacet getDescribedAsFacet(final Class<?> type) {
        final NakedObjectSpecification paramTypeSpec = getSpecificationLoader().loadSpecification(type);
        return paramTypeSpec.getFacet(DescribedAsFacet.class);
    }

}
