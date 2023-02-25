package org.nakedobjects.runtime.authorization;

import org.nakedobjects.metamodel.commons.component.Installer;


public interface AuthorizationManagerInstaller extends Installer {

	static String TYPE = "authorization";
	
    AuthorizationManager createAuthorizationManager();

}
