package org.nakedobjects.webapp.auth;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.authentication.standard.fixture.AuthenticationRequestLogonFixture;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.webapp.WebAppConstants;

/**
 * Looks up from the {@link HttpSession}, failing
 * that attempts to logon using a {@link LogonFixture} (if available and not
 * already used) or in exploration mode.
 * 
 * <p>
 * The {@link AuthenticationSession} is looked up from the {@link HttpSession} using
 * the value {@value WebAppConstants#HTTP_SESSION_AUTHENTICATION_SESSION_KEY}.
 * Because we only want to use the {@link LogonFixture} once, the {@link HttpSession}
 * also stores the value {@value WebAppConstants#HTTP_SESSION_LOGGED_ON_PREVIOUSLY_USING_LOGON_FIXTURE_KEY}
 * once a logon has occurred implicitly.
 */
public class AuthenticationSessionLookupStrategyDefault extends
		AuthenticationSessionLookupStrategyAbstract {

	public AuthenticationSession lookup(ServletRequest servletRequest,
			ServletResponse servletResponse) {

		// use previously authenticated session if available.
		HttpSession httpSession = getHttpSession(servletRequest);
		AuthenticationSession authSession = (AuthenticationSession) httpSession
			.getAttribute(WebAppConstants.HTTP_SESSION_AUTHENTICATION_SESSION_KEY);
		if (authSession != null) {
			boolean sessionValid = getAuthenticationManager().isSessionValid(authSession);
			if (sessionValid) {
				return authSession;
			}
		}
		
		// otherwise, look for LogonFixture and try to authenticate
		ServletContext servletContext = getServletContext(servletRequest);
		NakedObjectsSystem system = (NakedObjectsSystem) servletContext
		.getAttribute(WebAppConstants.NAKED_OBJECTS_SYSTEM_KEY);
		if (system == null) {
			// not expected to happen...
			return null;
		}
		LogonFixture logonFixture = system.getLogonFixture();
		
		// see if exploration is supported
		if (system.getDeploymentType().isExploring()) {
			authSession = getAuthenticationManager().authenticate(
					new AuthenticationRequestExploration(logonFixture));
			if (authSession != null) {
				return authSession;
			}
		}
		
		boolean loggedInUsingLogonFixture = httpSession.getAttribute(WebAppConstants.HTTP_SESSION_LOGGED_ON_PREVIOUSLY_USING_LOGON_FIXTURE_KEY) != null;
		if (logonFixture != null && !loggedInUsingLogonFixture) {
			httpSession.setAttribute(WebAppConstants.HTTP_SESSION_LOGGED_ON_PREVIOUSLY_USING_LOGON_FIXTURE_KEY, true);
			return getAuthenticationManager().authenticate(
					new AuthenticationRequestLogonFixture(logonFixture));
		}

		return null;
	}

	public void bind(ServletRequest servletRequest,
			ServletResponse servletResponse, AuthenticationSession authSession) {
		HttpSession httpSession = getHttpSession(servletRequest);
		httpSession.setAttribute(
				WebAppConstants.HTTP_SESSION_AUTHENTICATION_SESSION_KEY,
				authSession);
	}

	// //////////////////////////////////////////////////////////
	// Dependencies (from context)
	// //////////////////////////////////////////////////////////

	private static AuthenticationManager getAuthenticationManager() {
		return NakedObjectsContext.getAuthenticationManager();
	}

}
