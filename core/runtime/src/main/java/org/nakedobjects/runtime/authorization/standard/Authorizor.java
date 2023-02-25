package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;


public interface Authorizor extends ApplicationScopedComponent {

	/**
	 * Checked for each of the user's roles.
	 */
    public boolean isVisibleInRole(final String user, final Identifier identifier);

	/**
	 * Checked for each of the user's roles.
	 */
    public boolean isUsableInRole(final String role, final Identifier identifier);

}
