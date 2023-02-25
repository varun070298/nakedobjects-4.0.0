package org.nakedobjects.runtime.transaction;

public interface TransactionalClosureWithReturn<T> {

	public void preExecute();
	
	public T execute();
	
	public void onSuccess();
	
	public void onFailure();
}
