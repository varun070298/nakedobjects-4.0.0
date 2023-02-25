package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Removes all superclass methods of the class, but doesn't add any {@link Facet}s.
 */
public class RemoveSuperclassMethodsFacetFactory extends FacetFactoryAbstract {

    @SuppressWarnings("unused")
	private static final String JAVA_CLASS_PREFIX = "java.";

    public RemoveSuperclassMethodsFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        removeSuperclassMethods(type, methodRemover);
        return false;
    }

    private void removeSuperclassMethods(final Class<?> type, final MethodRemover methodRemover) {
        if (type == null) {
            return;
        }

        if (!JavaClassUtils.isJavaClass(type)) {
            removeSuperclassMethods(type.getSuperclass(), methodRemover);
            return;
        }

        final Method[] methods = type.getMethods();
        for (int j = 0; j < methods.length; j++) {
            final Method method = methods[j];
            methodRemover.removeMethod(method);
        }

    }

}

// Copyright (c) Naked Objects Group Ltd.
