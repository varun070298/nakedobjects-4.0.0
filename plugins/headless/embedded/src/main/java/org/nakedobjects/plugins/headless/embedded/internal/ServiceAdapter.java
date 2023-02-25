package org.nakedobjects.plugins.headless.embedded.internal;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public class ServiceAdapter extends StandaloneAdapter {

	
	public ServiceAdapter(NakedObjectSpecification spec, Object serviceObject) {
		super(spec, serviceObject, PersistenceState.PERSISTENT);
	}

}
