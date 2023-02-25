package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Removes all methods inherited from {@link Object}.
 */
public class RemoveJavaLangObjectMethodsFacetFactory extends FacetFactoryAbstract {

    public RemoveJavaLangObjectMethodsFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    private static final String[] objectMethodNames;
    private static final Class<?>[] objectMethodReturnTypes;
    private static final Class<?>[][] objectMethodParameters;

    static {
        final Method[] methods = Object.class.getMethods();
        final int size = methods.length;
        objectMethodNames = new String[size];
        objectMethodReturnTypes = new Class[size];
        objectMethodParameters = new Class[size][];
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            objectMethodNames[i] = method.getName();
            objectMethodReturnTypes[i] = method.getReturnType();
            objectMethodParameters[i] = method.getParameterTypes();
        }
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Method[] methods = Object.class.getMethods();
        for (int i = 0; i < methods.length; i++) {
            methodRemover.removeMethod(MethodScope.OBJECT, objectMethodNames[i], null, objectMethodParameters[i]);
        }

        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
