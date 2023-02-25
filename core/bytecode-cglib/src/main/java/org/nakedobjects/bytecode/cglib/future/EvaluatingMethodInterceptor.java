package org.nakedobjects.bytecode.cglib.future;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.nakedobjects.metamodel.commons.future.FutureResultFactory;

final class EvaluatingMethodInterceptor<T> implements
		MethodInterceptor {
	
	private final FutureResultFactory<T> resultFactory;
	private T result;
	
	public EvaluatingMethodInterceptor(FutureResultFactory<T> resultFactory) {
		this.resultFactory = resultFactory;
	}

	public synchronized Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		if (result == null) {
			result = resultFactory.getResult();
		}
		return method.invoke(result, args);
	}
}