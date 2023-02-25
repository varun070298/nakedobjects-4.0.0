package org.nakedobjects.metamodel.facets.hide;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class HiddenAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public HiddenAnnotationFacetFactory() {
        super(NakedObjectFeatureType.EVERYTHING_BUT_PARAMETERS);
    }

    
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Hidden annotation = getAnnotation(cls, Hidden.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Hidden annotation = getAnnotation(method, Hidden.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private HiddenFacet create(final Hidden annotation, final FacetHolder holder) {
        return annotation == null ? null : new HiddenFacetAnnotation(decodeWhen(annotation.value()), holder);
    }

}
