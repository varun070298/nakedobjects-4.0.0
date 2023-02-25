package org.nakedobjects.metamodel.commons.future;

/**
 * API, implemented by developer.
 */
public interface FutureResultFactory<T> {
	
	Class<T> getResultClass();
	
	T getResult();


}
