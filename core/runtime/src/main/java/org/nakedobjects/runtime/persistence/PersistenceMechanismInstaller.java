package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Installs a {@link PersistenceSession} during system start up.
 */
public interface PersistenceMechanismInstaller extends Installer, PersistenceSessionFactoryDelegate {

	static String TYPE = "persistor";
	
    PersistenceSessionFactory createPersistenceSessionFactory(DeploymentType deploymentType);


}
// Copyright (c) Naked Objects Group Ltd.
