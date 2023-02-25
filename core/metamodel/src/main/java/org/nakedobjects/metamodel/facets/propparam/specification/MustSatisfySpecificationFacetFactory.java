package org.nakedobjects.metamodel.facets.propparam.specification;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.annotation.MustSatisfy;
import org.nakedobjects.applib.spec.Specification;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class MustSatisfySpecificationFacetFactory  extends AnnotationBasedFacetFactoryAbstract {

    public MustSatisfySpecificationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(Class<?> clazz, MethodRemover methodRemover,
    		FacetHolder holder) {
        return FacetUtil.addFacet(create(clazz, holder));
    }

    private MustSatisfySpecificationFacet create(Class<?> clazz, FacetHolder holder) {
        return create(getAnnotation(clazz, MustSatisfy.class), holder);
    }


    @Override
    public boolean process(Class<?> cls, Method method, MethodRemover methodRemover, FacetHolder holder) {
        return FacetUtil.addFacet(create(method, holder));
    }

    private MustSatisfySpecificationFacet create(Method method, FacetHolder holder) {
        return create(getAnnotation(method, MustSatisfy.class), holder);
    }


    @Override
    public boolean processParams(Method method, int paramNum, FacetHolder holder) {
        final java.lang.annotation.Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];

        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof MustSatisfy) {
                final MustSatisfy annotation = (MustSatisfy) parameterAnnotations[j];
                return FacetUtil.addFacet(create(annotation, holder));
            }
        }
        return false;
    }
    
    private MustSatisfySpecificationFacet create(final MustSatisfy annotation, final FacetHolder holder) {
        if (annotation == null) {
            return null;
        }
        Class<?>[] values = annotation.value();
        List<Specification> specifications = new ArrayList<Specification>();
        for(Class<?> value: values) {
            Specification specification = newSpecificationElseNull(value);
            if (specification != null) {
                specifications.add(specification);
            }
        }
        return specifications.size() > 0 ? new MustSatisfySpecificationFacet(specifications, holder) : null;
    }

    private Specification newSpecificationElseNull(Class<?> value) {
        if (!(Specification.class.isAssignableFrom(value))) {
            return null;
        }
        try {
            return (Specification) value.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

}
