package org.nakedobjects.metamodel.facets.ignore;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Ignore;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class RemoveIgnoreAnnotationMethodsFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public RemoveIgnoreAnnotationMethodsFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        removeIgnoredMethods(cls, methodRemover);
        return false;
    }

    private void removeIgnoredMethods(final Class<?> cls, final MethodRemover methodRemover) {
        if (cls == null) {
            return;
        }

        final Method[] methods = cls.getMethods();
        for (int j = 0; j < methods.length; j++) {
            final Method method = methods[j];
            final Ignore annotation = getAnnotation(method, Ignore.class);
            if (annotation != null) {
                methodRemover.removeMethod(method);
            }
        }
    }

}
