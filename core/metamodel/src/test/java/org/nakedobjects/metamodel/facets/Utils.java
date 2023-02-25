package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


class Utils {

    protected static boolean contains(final Class<?>[] array, final Class<?> val) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == val) {
                return true;
            }
        }
        return false;
    }

    protected static boolean contains(final NakedObjectFeatureType[] array, final NakedObjectFeatureType val) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == val) {
                return true;
            }
        }
        return false;
    }

    protected static Method findMethod(final Class<?> type, final String methodName, final Class<?>[] methodTypes) {
        try {
            return type.getMethod(methodName, methodTypes);
        } catch (final SecurityException e) {
            return null;
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    protected static Method findMethod(final Class<?> type, final String methodName) {
        return findMethod(type, methodName, new Class[0]);
    }

}

// Copyright (c) Naked Objects Group Ltd.
