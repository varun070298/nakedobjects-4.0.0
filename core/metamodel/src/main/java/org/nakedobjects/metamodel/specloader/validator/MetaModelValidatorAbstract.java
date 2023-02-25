package org.nakedobjects.metamodel.specloader.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class MetaModelValidatorAbstract implements MetaModelValidator {

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
	// Dependencies (due to *Aware)
	//////////////////////////////////////////////////////////////////////

	public SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}
	public void setSpecificationLoader(SpecificationLoader specificationLoader) {
		this.specificationLoader = specificationLoader;
	}

}
