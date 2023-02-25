package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class InvocationHandlerMethodInterceptor implements MethodInterceptor {
	private final InvocationHandler handler;

	InvocationHandlerMethodInterceptor(InvocationHandler handler) {
		this.handler = handler;
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
	    return handler.invoke(obj, method, args);
	}
}