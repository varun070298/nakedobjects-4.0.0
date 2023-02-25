package org.nakedobjects.metamodel.facets.propcoll.notpersisted;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.NotPersisted;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public NotPersistedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_AND_COLLECTIONS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final NotPersisted annotation = getAnnotation(method, NotPersisted.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private NotPersistedFacet create(final NotPersisted annotation, final FacetHolder holder) {
        return annotation == null ? null : new NotPersistedFacetAnnotation(holder);
    }

}
