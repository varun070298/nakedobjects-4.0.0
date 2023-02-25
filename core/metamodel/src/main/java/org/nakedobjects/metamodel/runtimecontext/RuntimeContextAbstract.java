package org.nakedobjects.metamodel.runtimecontext;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerAware;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;


public abstract class RuntimeContextAbstract implements RuntimeContext, SpecificationLoaderAware, DomainObjectContainerAware {

	private SpecificationLoader specificationLoader;
	private DomainObjectContainer container;

	public RuntimeContextAbstract() {
	}

	public void injectInto(Object candidate) {
        if (RuntimeContextAware.class.isAssignableFrom(candidate.getClass())) {
        	RuntimeContextAware cast = RuntimeContextAware.class.cast(candidate);
            cast.setRuntimeContext(this);
        }
	}

	
	public SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}
	
	/**
	 * Is injected into when the reflector is {@link NakedObjectReflectorAbstract#init() initialized}.
	 */
	public void setSpecificationLoader(SpecificationLoader specificationLoader) {
		this.specificationLoader = specificationLoader;
	}
	
	

	protected DomainObjectContainer getContainer() {
		return container;
	}
	/**
	 * So that {@link #injectDependenciesInto(Object)} can also inject the {@link DomainObjectContainer}.
	 */
	public void setContainer(DomainObjectContainer container) {
		this.container = container;
	}
	


}
