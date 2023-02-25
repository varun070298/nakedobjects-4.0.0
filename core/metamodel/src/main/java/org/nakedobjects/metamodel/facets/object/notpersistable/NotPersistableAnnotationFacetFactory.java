package org.nakedobjects.metamodel.facets.object.notpersistable;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.NotPersistable;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistableAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public NotPersistableAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final NotPersistable annotation = getAnnotation(cls, NotPersistable.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final NotPersistable annotation = getAnnotation(method, NotPersistable.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private NotPersistableFacet create(final NotPersistable annotation, final FacetHolder holder) {
        return annotation != null ? new NotPersistableFacetAnnotation(decodeBy(annotation.value()), holder) : null;
    }

    private InitiatedBy decodeBy(final NotPersistable.By by) {
        if (by == NotPersistable.By.USER) {
            return InitiatedBy.USER;
        }
        if (by == NotPersistable.By.USER_OR_PROGRAM) {
            return InitiatedBy.USER_OR_PROGRAM;
        }
        return null;
    }

}
