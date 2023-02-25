package org.nakedobjects.metamodel.commons.lang;

import java.lang.reflect.Method;


public class MethodUtils {

    private MethodUtils() {}

    public static Method getMethod(final Object object, final String methodName, final Class<?>... parameterClass)
            throws NoSuchMethodException {
        return getMethod(object.getClass(), methodName, parameterClass);
    }

    public static Method getMethod(final Object object, final String methodName) throws NoSuchMethodException {
        return getMethod(object.getClass(), methodName, new Class[0]);
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterClass)
            throws NoSuchMethodException {
        return clazz.getMethod(methodName, parameterClass);
    }

    public static Method findMethodElseNull(final Class<?> clazz, final String methodName, final Class<?>... parameterClass) {
        try {
            return clazz.getMethod(methodName, parameterClass);
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    public static Method findMethodElseNull(
            final Class<?> clazz,
            final String[] candidateMethodNames,
            final Class<?>... parameterClass) {
        for (final String candidateMethodName : candidateMethodNames) {
            final Method method = findMethodElseNull(clazz, candidateMethodName, parameterClass);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

}
