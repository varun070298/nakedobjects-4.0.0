package org.nakedobjects.webapp.auth;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.runtime.authentication.standard.fixture.AuthenticationRequestLogonFixture;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.webapp.WebAppConstants;

public abstract class AuthenticationSessionLookupStrategyAbstract implements
		AuthenticationSessionLookupStrategy {

	protected HttpSession getHttpSession(ServletRequest servletRequest) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		return httpServletRequest.getSession();
	}

	protected ServletContext getServletContext(ServletRequest servletRequest) {
		HttpSession httpSession = getHttpSession(servletRequest);
		return httpSession.getServletContext();
	}
	
}
