package org.nakedobjects.runtime.bytecode;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.spec.JavaSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectChanger;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectResolver;

public abstract class ObjectResolveAndObjectChangedEnhancerAbstract {

	protected final ObjectResolver objectResolver;
	protected final ObjectChanger objectChanger;
	protected final SpecificationLoader specificationLoader;

	public ObjectResolveAndObjectChangedEnhancerAbstract(
			final ObjectResolver objectResolver,
			final ObjectChanger objectChanger,
			final SpecificationLoader specificationLoader) {
		ensureThatArg(objectResolver, is(notNullValue()));
		ensureThatArg(objectChanger, is(notNullValue()));
		ensureThatArg(specificationLoader, is(notNullValue()));

		this.objectResolver = objectResolver;
		this.objectChanger = objectChanger;
		this.specificationLoader = specificationLoader;
	}

	/**
	 * Subclasses should call from their constructor, and setup their
	 * implementation-specific callback mechanism.
	 */
	protected abstract void createCallback();

	protected JavaSpecification getJavaSpecificationOfOwningClass(final Method method) {
		return getJavaSpecification(method.getDeclaringClass());
	}

	protected JavaSpecification getJavaSpecification(final Class<?> cls) {
		final NakedObjectSpecification nos = getSpecification(cls);
		if (!(nos instanceof JavaSpecification)) {
			throw new UnsupportedOperationException(
					"Only Java is supported (specification is '"
							+ nos.getClass().getCanonicalName() + "')");
		}
		return (JavaSpecification) nos;
	}

	protected boolean impliesResolve(ImperativeFacet[] imperativeFacets) {
		for(ImperativeFacet imperativeFacet: imperativeFacets) {
			if (imperativeFacet.impliesResolve()) {
				return true;
			}
		}
		return false;
	}

	protected boolean impliesObjectChanged(ImperativeFacet[] imperativeFacets) {
		for(ImperativeFacet imperativeFacet: imperativeFacets) {
			if (imperativeFacet.impliesObjectChanged()) {
				return true;
			}
		}
		return false;
	}

	private NakedObjectSpecification getSpecification(final Class<?> type) {
		return specificationLoader.loadSpecification(type);
	}
	
	///////////////////////////////////////////////////////////////
	// Dependencies (from constructor)
	///////////////////////////////////////////////////////////////
	
	public final ObjectResolver getObjectResolver() {
		return objectResolver;
	}

	public final ObjectChanger getObjectChanger() {
		return objectChanger;
	}

	public final SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}

}