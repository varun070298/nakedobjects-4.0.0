package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;

public interface ImperativeFacetMulti extends ImperativeFacet {


	/**
	 * Associate an additional method,
	 * to be returned from {@link ImperativeFacet#getMethods()}.
	 * 
	 * @param method
	 */
	public void addMethod(final Method method);
}
