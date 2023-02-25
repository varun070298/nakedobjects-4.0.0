package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.nakedobjects.plugins.headless.applib.ViewObject;



public class JavaProxyFactory<T> implements IProxyFactory<T> {
    @SuppressWarnings("unchecked")
    public T createProxy(final T toProxy, final InvocationHandler handler) {
        final Class<T> proxyClass = (Class<T>) toProxy.getClass();
        return (T) Proxy.newProxyInstance(proxyClass.getClassLoader(), new Class[] { proxyClass }, handler);
    }

    @SuppressWarnings("unchecked")
    public T createProxy(final Class<T> toProxy, final InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(toProxy.getClassLoader(), new Class[] { toProxy, ViewObject.class }, handler);
    }
}
