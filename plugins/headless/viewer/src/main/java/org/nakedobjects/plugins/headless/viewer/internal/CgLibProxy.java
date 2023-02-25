package org.nakedobjects.plugins.headless.viewer.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal.CglibEnhanced;
import org.nakedobjects.plugins.headless.applib.ViewObject;




public class CgLibProxy<T> {

    private final DelegatingInvocationHandler<T> handler;

    public CgLibProxy(final DelegatingInvocationHandler<T> handler) {
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    public T proxy() {

    	final T toProxy = handler.getDelegate();
    	
    	// handle if already proxied using cglib.
    	if (CglibEnhanced.class.isAssignableFrom(toProxy.getClass())) {
    		
    		handler.setResolveObjectChangedEnabled(true);
    		
    		Class<? extends Object> enhancedClass = toProxy.getClass();
    		Class<? extends Object> origSuperclass = toProxy.getClass().getSuperclass();
    		
    		List<Class> interfaces = new ArrayList<Class>();
			interfaces.addAll(Arrays.asList(enhancedClass.getInterfaces()));
			interfaces.remove(Factory.class); // if there.
			interfaces.add(ViewObject.class);
    		
			return (T) Enhancer.create(
					origSuperclass,
					interfaces.toArray(new Class[]{}),
					new InvocationHandlerMethodInterceptor(handler));
    	}
    	
        final Class<T> clazz = (Class<T>) toProxy.getClass();
        
        T proxy = null;
        try {
            final IProxyFactory<T> proxyFactory = 
            	clazz.isInterface()?
					new JavaProxyFactory<T>():
					new CgLibClassProxyFactory<T>();
            proxy = proxyFactory.createProxy(clazz, handler);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
        return proxy;
    }



}
