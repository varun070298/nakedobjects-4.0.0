package org.nakedobjects.metamodel.specloader.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MetaModelValidatorComposite extends MetaModelValidatorAbstract {

	private List<MetaModelValidator> validators = new ArrayList<MetaModelValidator>();
	
	public void validate() throws MetaModelInvalidException {
		for (MetaModelValidator validator : validators) {
			validator.validate();
		}
	}
	
	
	public void addValidator(MetaModelValidator validator) {
		validators.add(validator);
	}
	
	public List<MetaModelValidator> getValidators() {
		return Collections.unmodifiableList(validators);
	}


}
