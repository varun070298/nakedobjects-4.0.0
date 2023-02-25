package org.nakedobjects.plugins.html.servlet.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.PasswordAuthenticationRequest;
import org.nakedobjects.runtime.authentication.standard.ExplorationSession;
import org.nakedobjects.runtime.authentication.standard.SimpleSession;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.DeploymentType;


public class SessionAccess {

    private static final Logger LOG = Logger.getLogger(SessionAccess.class);
    private static SessionAccess instance;

    // //////////////////////////////////////////////
    // Singleton factory
    // //////////////////////////////////////////////

    /**
     * Create singleton
     */
    public static void createInstance(final AuthenticationManager authenticationManager) {
        new SessionAccess(authenticationManager);
    }

    /**
     * Create singleton
     */
    public static void createInstance(
            final AuthenticationManager authenticationManager,
            final DeploymentType deploymentType,
            final LogonFixture logonFixture) {
        new SessionAccess(authenticationManager, deploymentType, logonFixture);
    }

    // //////////////////////////////////////////////
    // Sessions
    // //////////////////////////////////////////////

    // REVIEW part of decoupling systemaccess from servlet code - moved list sessions to monitorservlet
    public static List<Object> getSessions() {
        return instance.sessions;
    }

    // REVIEW part of decoupling systemaccess from servlet code - changed session to object
    public static void addSession(final Object session) {
        instance.sessions.add(session);
        if (LOG.isDebugEnabled()) {
            LOG.debug("session started " + session);
        }
    }

    // REVIEW part of decoupling systemaccess from servlet code - moved logoff part to sessionlist
    public static void removeSession(final Object session) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("session ended " + session);
        }
        instance.sessions.remove(session);
    }

    // //////////////////////////////////////////////
    // Authentication, logoff
    // //////////////////////////////////////////////

    public static AuthenticationSession authenticate(final PasswordAuthenticationRequest passwordAuthenticationRequest) {
        return instance.authenticateInstance(passwordAuthenticationRequest);
    }

    public static void logoffUser(final AuthenticationSession session) {
        getAuthenticationManager().closeSession(session);
    }

    private static AuthenticationManager getAuthenticationManager() {
        if (instance == null) {
            throw new IllegalStateException("Server initialisation failed, or not defined as a context listener");
        }
        return instance.authenticationManager;
    }

    // //////////////////////////////////////////////
    // Request
    // //////////////////////////////////////////////

    public static void startRequest(final AuthenticationSession authSession) {
        NakedObjectsContext.openSession(authSession);
    }

    public static void endRequest(final AuthenticationSession authSession) {
    	// if a Logout has occurred then there won't be any session
    	// (though should the AuthenticationManagerStandard actually be closing the session? not sure)
    	
    	if (NakedObjectsContext.inSession()) {
    		NakedObjectsContext.closeSession();
    	}
    }

    // //////////////////////////////////////////////
    // Settings
    // //////////////////////////////////////////////

    public static boolean inExplorationMode() {
        return instance.inExplorationModeInstance();
    }

    // ///////////////////////////////////////////////////////////////////////////

    private final List<Object> sessions = new ArrayList<Object>();

    private final AuthenticationManager authenticationManager;
    private DeploymentType deploymentType;
    private LogonFixture logonFixture;

    private SessionAccess(final AuthenticationManager authenticationManager) {
        this(authenticationManager, DeploymentType.STANDALONE, null);
    }

    private SessionAccess(
            final AuthenticationManager authenticationManager,
            final DeploymentType deploymentType,
            final LogonFixture logonFixture) {
        this.authenticationManager = authenticationManager;

        this.deploymentType = deploymentType;
        this.logonFixture = logonFixture;

        SessionAccess.instance = this;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public LogonFixture getLogonFixture() {
        return logonFixture;
    }

    private AuthenticationSession authenticateInstance(PasswordAuthenticationRequest passwordAuthenticationRequest) {

        if ((deploymentType.isExploring() || deploymentType.isPrototyping()) && 
             logonFixture != null) {
            return new SimpleSession(logonFixture.getUsername(), logonFixture.getRoles());
        }

        if (deploymentType.isExploring()) {
            return new ExplorationSession();
        }

        return authenticationManager.authenticate(passwordAuthenticationRequest);
    }

    private boolean inExplorationModeInstance() {
        return deploymentType.isExploring();
    }

}

// Copyright (c) Naked Objects Group Ltd.
