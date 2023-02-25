package org.nakedobjects.metamodel.commons.future;

public abstract class FutureResultFactoryAbstract<T> implements FutureResultFactory<T> {
	
	private final Class<T> resultClass;

	public FutureResultFactoryAbstract(Class<T> resultClass) {
		this.resultClass = resultClass;
	}
	public Class<T> getResultClass() {
		return resultClass;
	}
	
}
