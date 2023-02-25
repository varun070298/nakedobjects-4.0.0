package org.nakedobjects.metamodel.facets.actions.exploration;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Exploration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ExplorationAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public ExplorationAnnotationFacetFactory() {
        super(NakedObjectFeatureType.ACTIONS_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Exploration annotation = getAnnotation(method, Exploration.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private ExplorationFacet create(final Exploration annotation, final FacetHolder holder) {
        return annotation == null ? null : new ExplorationFacetAnnotation(holder);
    }

}
