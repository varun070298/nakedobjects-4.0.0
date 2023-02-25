package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ProgrammableMethodRemover implements MethodRemover {

    // ////////////////////////////////////////////////////////////
    // removeMethod(...): void
    // ////////////////////////////////////////////////////////////

    static class RemoveMethodArgs {
        public RemoveMethodArgs(
                final MethodScope methodScope,
                final String methodName,
                final Class<?> returnType,
                final Class<?>[] parameterTypes) {
            this.methodScope = methodScope;
            this.methodName = methodName;
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
        }
        public MethodScope methodScope;
        public String methodName;
        public Class<?> returnType;
        public Class<?>[] parameterTypes;
    }

    private final List<RemoveMethodArgs> removeMethodArgsCalls = new ArrayList<RemoveMethodArgs>();

    public void removeMethod(
            final MethodScope methodScope,
            final String methodName,
            final Class<?> returnType,
            final Class<?>[] parameterTypes) {
        removeMethodArgsCalls.add(new RemoveMethodArgs(methodScope, methodName, returnType, parameterTypes));
    }

    public List<RemoveMethodArgs> getRemoveMethodArgsCalls() {
        return removeMethodArgsCalls;
    }

    // ////////////////////////////////////////////////////////////
    // removeMethod(Method): void
    // ////////////////////////////////////////////////////////////

    private final List<Method> removeMethodMethodCalls = new ArrayList<Method>();

    public void removeMethod(final Method method) {
        removeMethodMethodCalls.add(method);
    }

    public List<Method> getRemoveMethodMethodCalls() {
        return removeMethodMethodCalls;
    }

    // ////////////////////////////////////////////////////////////
    // removeMethods(...):List
    // ////////////////////////////////////////////////////////////

    private List<Method> removeMethodsReturn;

    public void setRemoveMethodsReturn(final List<Method> removeMethodsReturn) {
        this.removeMethodsReturn = removeMethodsReturn;
    }

    static class RemoveMethodsArgs {
        public RemoveMethodsArgs(
                final MethodScope methodScope,
                final String prefix,
                final Class<?> returnType,
                final boolean canBeVoid,
                final int paramCount) {
            this.methodScope = methodScope;
            this.prefix = prefix;
            this.returnType = returnType;
            this.canBeVoid = canBeVoid;
            this.paramCount = paramCount;
        }
        public MethodScope methodScope;
        public String prefix;
        public Class<?> returnType;
        public boolean canBeVoid;
        public int paramCount;
    }
    private final List<RemoveMethodsArgs> removeMethodsArgs = new ArrayList<RemoveMethodsArgs>();

    public List<Method> removeMethods(
            final MethodScope methodScope,
            final String prefix,
            final Class<?> returnType,
            final boolean canBeVoid,
            final int paramCount) {
        removeMethodsArgs.add(new RemoveMethodsArgs(methodScope, prefix, returnType, canBeVoid, paramCount));
        return removeMethodsReturn;
    }

    // ////////////////////////////////////////////////////////////
    // removeMethods(List):void
    // ////////////////////////////////////////////////////////////

    public void removeMethods(final List<Method> methods) {
        for (Method method: methods) {
            removeMethod(method);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
