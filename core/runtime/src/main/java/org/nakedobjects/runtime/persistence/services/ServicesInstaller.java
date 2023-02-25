package org.nakedobjects.runtime.persistence.services;

import java.util.List;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.runtime.system.DeploymentType;


public interface ServicesInstaller extends Installer {
	
	/**
	 * NB: this has the suffix '-installer' because in the command line we must 
	 * distinguish from the '--services' flag meaning a particular set of services to use
	 * (whereas this flag means how to locate them).
	 */
	static String TYPE = "services-installer";

    List<Object> getServices(DeploymentType deploymentType);
}

// Copyright (c) Naked Objects Group Ltd.
