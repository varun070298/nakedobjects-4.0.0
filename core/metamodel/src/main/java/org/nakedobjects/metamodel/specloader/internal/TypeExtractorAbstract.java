package org.nakedobjects.metamodel.specloader.internal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class factoring out common functionality for
 * helper methods that extract parameterized types.
 * 
 */
abstract class TypeExtractorAbstract implements Iterable<Class<?>>{
    private final Method method;
    private final List<Class<?>> classes = new ArrayList<Class<?>>();
    
    public TypeExtractorAbstract(final Method method) {
        this.method = method;
    }

    protected void addParameterizedTypes(Type... genericTypes) {
        for(Type genericType: genericTypes) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                for(Type type: typeArguments) {
                    if (type instanceof Class) {
                        Class<?> cls = (Class<?>) type;
                        add(cls);
                    }
                }
            }
        }
    }
    
    /**
     * Adds to {@link #getClasses() list of classes}, provided not
     * {@link Void}.
     */
    protected void add(Class<?> cls) {
        if (cls == void.class) {
            return;
        }
        classes.add(cls);
    }

    /**
     * The {@link Method} provided in the {@link #TypeExtractorAbstract(Method) constructor.}
     */
    protected Method getMethod() {
        return method;
    }

    public List<Class<?>> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public Iterator<Class<?>> iterator() {
        return getClasses().iterator();
    }
}

// Copyright (c) Naked Objects Group Ltd.
