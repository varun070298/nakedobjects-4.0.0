package org.nakedobjects.webapp;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectsSystem;

public final class WebAppConstants {
	
	/**
	 * Name of context-param (<tt>ServletContext#getInitParameter(String)</tt>)
	 * to specify the deployment type.
	 */
	public static final String DEPLOYMENT_TYPE_KEY = "deploymentType";
	/**
	 * Deployment type to use if there is none {@link DEPLOYMENT_TYPE_KEY specified}.
	 */
	public static final String DEPLOYMENT_TYPE_DEFAULT = DeploymentType.SERVER.name();
	
	/**
	 * Key under which the {@link NakedObjectsSystem} is bound as an
	 * servlet context attribute (<tt>ServletContext#getAttribute(String)</tt>).
	 */
	public final static String NAKED_OBJECTS_SYSTEM_KEY = WebAppConstants.class
			.getPackage().getName()
			+ ".nakedObjectsSystem";
	/**
	 * Key under which the {@link AuthenticationSession} is bound as a
	 * session attribute (<tt>HttpSession#getAttribute(String)</tt>).
	 */
	public final static String HTTP_SESSION_AUTHENTICATION_SESSION_KEY = WebAppConstants.class
			.getPackage().getName()
			+ ".authenticationSession";

	/**
	 * Key used to determine if a logon has already been performed implicitly using the
	 * {@link LogonFixture}, meaning that a Logout should be followed by the Logon page.
	 */
	public final static String HTTP_SESSION_LOGGED_ON_PREVIOUSLY_USING_LOGON_FIXTURE_KEY = WebAppConstants.class
			.getPackage().getName()
			+ ".loggedOnPreviouslyUsingLogonFixture";

	private WebAppConstants(){}

}
