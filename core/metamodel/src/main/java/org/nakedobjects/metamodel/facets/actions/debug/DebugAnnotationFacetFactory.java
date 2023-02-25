package org.nakedobjects.metamodel.facets.actions.debug;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Debug;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DebugAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public DebugAnnotationFacetFactory() {
        super(NakedObjectFeatureType.ACTIONS_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Debug annotation = getAnnotation(method, Debug.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private DebugFacet create(final Debug annotation, final FacetHolder holder) {
        return annotation == null ? null : new DebugFacetAnnotation(holder);
    }

}
