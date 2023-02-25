package org.nakedobjects.metamodel.facets.ordering.memberorder;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class MemberOrderAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public MemberOrderAnnotationFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_COLLECTIONS_AND_ACTIONS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        Class<MemberOrder> annotationClass = MemberOrder.class;
		final MemberOrder annotation = getAnnotation(method, annotationClass);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private MemberOrderFacet create(final MemberOrder annotation, final FacetHolder holder) {
        return annotation == null ? null : new MemberOrderFacetAnnotation(annotation.name(), annotation.sequence(), holder);
    }

}
