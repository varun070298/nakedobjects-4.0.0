package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.RegEx;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class RegExAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public RegExAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final RegEx annotation = getAnnotation(cls, RegEx.class);
        return FacetUtil.addFacet(createRegexFacet(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Class<?> returnType = method.getReturnType();
        if (!isString(returnType)) {
            return false;
        }
        final RegEx annotation = getAnnotation(method, RegEx.class);
        return addRegexFacetAndCorrespondingTitleFacet(holder, annotation);
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
            if (parameterAnnotations[j] instanceof RegEx) {
                final RegEx annotation = (RegEx) parameterAnnotations[j];
                return addRegexFacetAndCorrespondingTitleFacet(holder, annotation);
            }
        }
        return false;
    }

    private boolean addRegexFacetAndCorrespondingTitleFacet(final FacetHolder holder, final RegEx annotation) {
        final RegExFacet regexFacet = createRegexFacet(annotation, holder);
        if (regexFacet == null) {
            return false;
        }
        FacetUtil.addFacet(regexFacet);

        final TitleFacet titleFacet = createTitleFacet(regexFacet);
        return FacetUtil.addFacet(titleFacet);
    }

    private RegExFacet createRegexFacet(final RegEx annotation, final FacetHolder holder) {
        if (annotation == null) {
            return null;
        }

        final String validationExpression = annotation.validation();
        final boolean caseSensitive = annotation.caseSensitive();
        final String formatExpression = annotation.format();

        return new RegExFacetAnnotation(validationExpression, formatExpression, caseSensitive, holder);
    }

    private TitleFacet createTitleFacet(final RegExFacet regexFacet) {
        return new TitleFacetFormattedByRegex(regexFacet);
    }

}
