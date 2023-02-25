package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationHandler;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

import org.nakedobjects.plugins.headless.applib.ViewObject;



/**
 * Factory generating a mock for a class.
 * <p>
 * Note that this class is stateful
 */
public class CgLibClassProxyFactory<T> implements IProxyFactory<T> {

    @SuppressWarnings("unchecked")
    public T createProxy(final T toProxy, final InvocationHandler handler) {
        final Class<T> proxyClass = (Class<T>) toProxy.getClass();
        return createProxy(proxyClass, handler);
    }

    @SuppressWarnings("unchecked")
    public T createProxy(final Class<T> toProxyClass, final InvocationHandler handler) {

        final MethodInterceptor interceptor = new InvocationHandlerMethodInterceptor(handler);

        // Create the proxy
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(toProxyClass);
        enhancer.setInterfaces(new Class[] { ViewObject.class });
        enhancer.setCallbackType(interceptor.getClass());

        final Class enhancedClass = enhancer.createClass();
        
        Enhancer.registerCallbacks(enhancedClass, new Callback[] { interceptor });

        Factory factory;
        try {
            factory = (Factory) ClassInstantiatorFactoryCE.getInstantiator().newInstance(enhancedClass);
        } catch (final InstantiationException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("Fail to instantiate mock for " + toProxyClass + " on "
                    + ClassInstantiatorFactoryCE.getJVM() + " JVM");
            // ///CLOVER:ON
        }

        // the below comment was from EasyMock code; don't think applies so commented out.
        
//        // This call is required. Cglib has some "magic code" making sure a
//        // callback is used by only one instance of a given class. So only the
//        // instance created right after registering the callback will get it.
//        // However, this is done in the construtor which I'm bypassing to
//        // allow class instantiation without calling a constructor.
//        // Fortunatly, the "magic code" is also called in getCallback which is
//        // why I'm calling it here mock.getCallback(0);
//        factory.getCallback(0);

        return (T) factory;
    }

}
