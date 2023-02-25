package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MethodRemoverConstants {

    public static MethodRemover NULL = new MethodRemover() {
        public List<Method> removeMethods(
                final MethodScope methodScope,
                final String prefix,
                final Class<?> returnType,
                final boolean canBeVoid,
                final int paramCount) {
            return new ArrayList<Method>();
        }

        public void removeMethod(
                final MethodScope methodScope,
                final String methodName,
                final Class<?> returnType,
                final Class<?>[] parameterTypes) {}

        public void removeMethod(final Method method) {}

        public void removeMethods(final List<Method> methods) {}
    };
}
