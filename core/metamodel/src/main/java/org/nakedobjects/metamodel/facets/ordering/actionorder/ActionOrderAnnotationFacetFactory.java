package org.nakedobjects.metamodel.facets.ordering.actionorder;

import org.nakedobjects.applib.annotation.ActionOrder;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ActionOrderAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public ActionOrderAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final ActionOrder annotation = (ActionOrder) getAnnotation(cls, ActionOrder.class);
        return FacetUtil.addFacet(create(annotation, facetHolder));
    }

    private ActionOrderFacet create(final ActionOrder annotation, final FacetHolder facetHolder) {
        return annotation == null ? null : new ActionOrderFacetAnnotation(annotation.value(), facetHolder);
    }

}
