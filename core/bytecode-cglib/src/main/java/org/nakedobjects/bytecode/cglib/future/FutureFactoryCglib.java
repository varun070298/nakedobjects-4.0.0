package org.nakedobjects.bytecode.cglib.future;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import net.sf.cglib.proxy.Enhancer;

import org.nakedobjects.metamodel.commons.future.FutureFactory;
import org.nakedobjects.metamodel.commons.future.FutureResultFactory;
import org.nakedobjects.metamodel.commons.lang.ArrayUtils;

public class FutureFactoryCglib implements FutureFactory {

	/**
	 * Cache of Enhancers, lazy populated.
	 */
	private final Map<FutureResultFactory<?>, Enhancer> enhancerByResultFactory = 
		new HashMap<FutureResultFactory<?>, Enhancer>();
	
	public FutureFactoryCglib() {
		// nothing to do
	}

	public <T> T createFuture(FutureResultFactory<T> resultFactory) {
		return newInstance(resultFactory);
	}

	@SuppressWarnings("unchecked")
	private <T> T newInstance(FutureResultFactory<T> resultFactory) {
		Enhancer enhancer = lookupOrCreateEnhancerFor(resultFactory);
		return (T) enhancer.create();
	}
	
	private <T> Enhancer lookupOrCreateEnhancerFor(FutureResultFactory<T> resultFactory) {
		
		Enhancer enhancer = enhancerByResultFactory.get(resultFactory);
		Class<T> cls = resultFactory.getResultClass();
		if (enhancer == null) {
			enhancer = new Enhancer();
			enhancer.setSuperclass(cls);
			enhancer.setInterfaces(ArrayUtils.combine(
					cls.getInterfaces(),
					new Class<?>[] { Future.class }));
			enhancer.setCallback(new EvaluatingMethodInterceptor<T>(resultFactory));
			enhancerByResultFactory.put(resultFactory, enhancer);
		}
		return enhancer;
	}

}
