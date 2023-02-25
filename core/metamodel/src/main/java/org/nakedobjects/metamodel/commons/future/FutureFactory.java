package org.nakedobjects.metamodel.commons.future;

/**
 * SPI, for example by implemented by cglib or javassist.
 */
public interface FutureFactory {
	
	public <T> T createFuture(FutureResultFactory<T> resultFactory);

}
