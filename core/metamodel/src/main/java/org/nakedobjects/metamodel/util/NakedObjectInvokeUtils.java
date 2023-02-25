package org.nakedobjects.metamodel.util;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;

public final class NakedObjectInvokeUtils {
	
	private NakedObjectInvokeUtils() {}
	
    public static Object invoke(final Method method, final NakedObject adapter) {
    	return InvokeUtils.invoke(method, NakedObjectUtils.unwrap(adapter));
    }

    public static void invoke(final List<Method> methods, final NakedObject adapter) {
    	InvokeUtils.invoke(methods, NakedObjectUtils.unwrap(adapter));
    }

    public static Object invoke(final Method method, final NakedObject adapter, final NakedObject arg0Adapter) {
        return InvokeUtils.invoke(method, 
        		NakedObjectUtils.unwrap(adapter), 
        		new Object[] { NakedObjectUtils.unwrap(arg0Adapter)});
    }

    public static Object invoke(final Method method, final NakedObject adapter, final NakedObject[] argumentAdapters) {
        return InvokeUtils.invoke(method, 
        		NakedObjectUtils.unwrap(adapter), 
        		NakedObjectUtils.unwrap(argumentAdapters));
    }

}
