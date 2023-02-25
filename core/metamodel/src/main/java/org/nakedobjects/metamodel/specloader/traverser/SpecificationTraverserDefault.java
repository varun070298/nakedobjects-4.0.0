package org.nakedobjects.metamodel.specloader.traverser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;
import org.nakedobjects.metamodel.specloader.internal.TypeExtractorMethodReturn;

public class SpecificationTraverserDefault implements SpecificationTraverser, SpecificationLoaderAware {

	private SpecificationLoader specificationLoader;
	

	//////////////////////////////////////////////////////////////////////
	// init, shutdown
	//////////////////////////////////////////////////////////////////////

	public void init() {
		ensureThatState(specificationLoader, is(notNullValue()));
	}

	public void shutdown() {
	}

	
	//////////////////////////////////////////////////////////////////////
	// Traverse API
	//////////////////////////////////////////////////////////////////////

	/**
	 * Traverses the return types of each method.
	 * 
	 * <p>
	 * It's possible for there to be multiple return types: the generic type, and the parameterized type.
	 */
	public void traverseTypes(Method method, List<Class<?>> discoveredTypes) {
		TypeExtractorMethodReturn returnTypes = new TypeExtractorMethodReturn(method);
		for (Class<?> returnType : returnTypes) {
			discoveredTypes.add(returnType);
		}
	}


	/**
	 * Does nothing.
	 */
	public void traverseReferencedClasses(NakedObjectSpecification noSpec,
			List<Class<?>> discoveredTypes)
			throws ClassNotFoundException {
	}


	
	//////////////////////////////////////////////////////////////////////
	// Dependencies (due to *Aware)
	//////////////////////////////////////////////////////////////////////
	
	public SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}

	public void setSpecificationLoader(SpecificationLoader specificationLoader) {
		this.specificationLoader = specificationLoader;
	}
	


}
