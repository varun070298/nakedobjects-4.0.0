package org.nakedobjects.runtime.persistence.objectfactory;

import java.lang.reflect.Modifier;

import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.objectstore.inmemory.InMemoryObjectStore;
import org.nakedobjects.runtime.persistence.PersistenceSession;

/**
 * Abstract adapter for {@link ObjectFactory}.
 * 
 * <p>
 * Implementation note: rather than use the <tt>*Aware</tt> interfaces, we
 * instead look up dependencies from the {@link NakedObjectsContext}. This is
 * necessary, for the {@link PersistenceSession} at least, because class
 * enhancers may hold a reference to the factory as part of the generated
 * bytecode. Since the {@link PersistenceSession} could change over the lifetime
 * of the instance (eg when using the {@link InMemoryObjectStore}), we must
 * always look the {@link PersistenceSession} from the
 * {@link NakedObjectsContext}. The same applies to the {@link ServicesInjector}.
 * 
 * <p>
 * In theory it would be possible to cache the {@link SpecificationLoader} and
 * inject using {@link SpecificationLoaderAware}, but since we are already using
 * the {@link NakedObjectsContext}, decided instead to use the same approach throughout.
 */
public abstract class ObjectFactoryAbstract implements ObjectFactory {

	private final Mode mode;

	public enum Mode {
		/**
		 * Fail if no {@link NakedObjectPersistor} has been injected.
		 */
		STRICT,
		/**
		 * Ignore if no {@link NakedObjectPersistor} has been injected (intended
		 * for testing only).
		 */
		RELAXED
	}

	public ObjectFactoryAbstract() {
		this(Mode.STRICT);
	}

	public ObjectFactoryAbstract(Mode mode) {
		this.mode = mode;
	}

	public <T> T instantiate(Class<T> cls) throws ObjectInstantiationException {

		if (mode == Mode.STRICT && getServicesInjector() == null) {
			throw new IllegalStateException(
					"ServicesInjector has not been injected into ObjectFactory");
		}
		if (Modifier.isAbstract(cls.getModifiers())) {
			throw new ObjectInstantiationException(
					"Cannot create an instance of an abstract class: " + cls);
		}
		T newInstance = doInstantiate(cls);

		if (getServicesInjector() != null) {
			getServicesInjector().injectDependencies(newInstance);
		}
		return newInstance;
	}

	// /////////////////////////////////////////////////////////////////
	// open, close
	// /////////////////////////////////////////////////////////////////

	/**
	 * Default implementation does nothing.
	 */
	public void open() {
	}

	/**
	 * Default implementation does nothing.
	 */
	public void close() {
	}

	// /////////////////////////////////////////////////////////////////
	// doInstantiate
	// /////////////////////////////////////////////////////////////////

	/**
	 * Hook method for subclasses to override.
	 */
	protected abstract <T> T doInstantiate(Class<T> cls)
			throws ObjectInstantiationException;

	// /////////////////////////////////////////////////////////////////
	// Dependencies (looked up from context)
	// /////////////////////////////////////////////////////////////////

	protected SpecificationLoader getSpecificationLoader() {
		return NakedObjectsContext.getSpecificationLoader();
	}

	protected PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}

	protected ServicesInjector getServicesInjector() {
		return getPersistenceSession().getServicesInjector();
	}

}
