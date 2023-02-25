package org.nakedobjects.metamodel.specloader.validator;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;

public interface MetaModelValidator extends SpecificationLoaderAware, ApplicationScopedComponent {
	
	public void validate() throws MetaModelInvalidException;
	

}
