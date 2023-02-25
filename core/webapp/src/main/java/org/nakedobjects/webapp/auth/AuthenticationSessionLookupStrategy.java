package org.nakedobjects.webapp.auth;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.webapp.NakedObjectsSessionFilter;

/**
 * Decouples the {@link NakedObjectsSessionFilter} from the mechanism of obtaining
 * the {@link AuthenticationSession}.
 */
public interface AuthenticationSessionLookupStrategy {

	AuthenticationSession lookup(ServletRequest servletRequest, ServletResponse servletResponse);
	
	void bind(ServletRequest servletRequest, ServletResponse servletResponse, AuthenticationSession authSession);
}
