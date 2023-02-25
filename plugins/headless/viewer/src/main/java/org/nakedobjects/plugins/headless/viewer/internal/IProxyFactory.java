package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationHandler;


public interface IProxyFactory<T> {
    T createProxy(Class<T> toProxyClass, InvocationHandler handler);

    T createProxy(T toProxy, InvocationHandler handler);
}
