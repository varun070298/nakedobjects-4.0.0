package org.nakedobjects.plugins.html.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.html.component.html.HtmlComponentFactory;
import org.nakedobjects.plugins.html.component.html.LogonFormPage;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.internal.monitor.Monitor;
import org.nakedobjects.webapp.WebAppConstants;
import org.nakedobjects.webapp.auth.AuthenticationSessionLookupStrategyDefault;


public class LogonServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(LogonServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
       
        
        AuthenticationSession authSession = new AuthenticationSessionLookupStrategyDefault().lookup(request, response);
        if (authSession != null) {
            boolean sessionValid = NakedObjectsContext.getAuthenticationManager().isSessionValid(authSession);
            if (sessionValid) {
                loggedIn(response, authSession.getUserName());
                return;
            }
        }

        String user = request.getParameter("username");
        final String password = request.getParameter("password");

        if (user == null && !NakedObjectsContext.getDeploymentType().isExploring()) {
            prompt(response, "", "", "");
            return;
        }

        authSession = authenticate(user, password);
        if (authSession == null) {
            prompt(response, user, password, "error");
            return;
        }

        final HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(WebAppConstants.HTTP_SESSION_AUTHENTICATION_SESSION_KEY, authSession);

        final Context context = new Context(new HtmlComponentFactory());
        context.setSession(authSession);
        authSession.setAttribute(HtmlServletConstants.AUTHENTICATION_SESSION_CONTEXT_KEY, context);

        LOG.info("created session: " + httpSession);
        loggedIn(response, user);
    }

    private AuthenticationSession authenticate(String user, String password) {
        AuthenticationRequest request;
        if (NakedObjectsContext.getDeploymentType() == DeploymentType.EXPLORATION) { 
            request = new AuthenticationRequestExploration();
        }else {
            request = new AuthenticationRequestPassword(user, password);
        }
        return getAuthenticationManager().authenticate(request);
    }

    private void prompt(final HttpServletResponse response, final String user, final String password, final String message)
            throws IOException {
        response.setContentType("text/html");
        final HtmlComponentFactory factory = new HtmlComponentFactory();
        final LogonFormPage page = factory.createLogonPage(user, password);
        page.write(response.getWriter());
    }

    private void loggedIn(final HttpServletResponse response, final String user) throws IOException {
        Monitor.addEvent("Web", "Logon - " + user);
        response.sendRedirect("start.app");
    }

    // //////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // //////////////////////////////////////////////////////////////

    private static AuthenticationManager getAuthenticationManager() {
        return NakedObjectsContext.getAuthenticationManager();
    }

}

// Copyright (c) Naked Objects Group Ltd.
