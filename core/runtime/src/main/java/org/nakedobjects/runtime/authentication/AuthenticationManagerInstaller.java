package org.nakedobjects.runtime.authentication;

import org.nakedobjects.metamodel.commons.component.Installer;


public interface AuthenticationManagerInstaller extends Installer {

	static String TYPE = "authentication";

    AuthenticationManager createAuthenticationManager();

}
