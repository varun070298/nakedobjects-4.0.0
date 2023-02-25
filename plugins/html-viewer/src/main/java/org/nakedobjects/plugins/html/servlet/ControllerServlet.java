package org.nakedobjects.plugins.html.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.html.HtmlComponentFactory;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.plugins.html.request.ServletRequest;
import org.nakedobjects.plugins.html.servlet.internal.WebController;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ControllerServlet.class);
    
    private String encoding = HtmlServletConstants.ENCODING_DEFAULT;
    private WebController controller;

    
    ////////////////////////////////////////////////////////////////////
    // init
    ////////////////////////////////////////////////////////////////////

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        encoding = getConfiguration().getString(HtmlServletConstants.ENCODING_KEY, encoding);
        
        controller = new WebController();
        controller.setDebug(getConfiguration().getBoolean(HtmlServletConstants.DEBUG_KEY));
        controller.init();
    }



    ////////////////////////////////////////////////////////////////////
    // doGet, doPost
    ////////////////////////////////////////////////////////////////////

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding(encoding);
        processRequest(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        processRequest(request, response);
    }

    private void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        LOG.info("request: " + request.getServletPath() + "?" + request.getQueryString());

        final Request req = new ServletRequest(request);

        if (req.getRequestType() == null) {
            throw new ServletException("No action specified");
        } else if (!controller.actionExists(req)) {
            throw new ServletException("No such action " + req.getRequestType());
        } else {
            try {
                final Context context = getContextForRequest(request);
                processRequest(request, response, req, context);
            } catch (final Exception e) {
                LOG.error("exception during request handling", e);
                throw new ServletException("Internal exception", e);
            }
        }
    }

    private Context getContextForRequest(final HttpServletRequest request) {
        AuthenticationSession authenticationSession = getAuthenticationSession();
		Context context = (Context) authenticationSession.getAttribute(HtmlServletConstants.AUTHENTICATION_SESSION_CONTEXT_KEY);
        if (context == null || !context.isValid()) {
            // TODO reuse the component factory
            context = new Context(new HtmlComponentFactory());
            authenticationSession.setAttribute(HtmlServletConstants.AUTHENTICATION_SESSION_CONTEXT_KEY, context);
        }
        return context;
    }


    private void processRequest(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Request req,
            final Context context) throws IOException, ServletException {
        response.setContentType("text/html");

        // no need to check if logged in; the NakedObjectsSessionFilter would
        // have prevented us from getting here.
        
        try {
            //SessionAccess.startRequest(context.getSession());
            final Page page = controller.generatePage(context, req);
            if (context.isValid()) {
                if (controller.isDebug()) {
                    controller.addDebug(page, req);
                    addDebug(request, page);
                }
                PrintWriter writer;
                writer = response.getWriter();
                page.write(writer);
            } else {
                response.sendRedirect(HtmlServletConstants.LOGON_APP_PAGE);
            }
        } finally {
            //SessionAccess.endRequest(context.getSession());
            if (!context.isLoggedIn()) {
                final HttpSession httpSession = request.getSession(false);
                LOG.info("dropping session: " + httpSession);
            }
        }
    }

    private void addDebug(final HttpServletRequest request, final Page page) {
        page.addDebug("Servlet path", request.getServletPath());
        page.addDebug("Query string", request.getQueryString());
        page.addDebug("Context path", request.getContextPath());
        page.addDebug("Path info", request.getPathInfo());
    }

    
    ////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////////////////
    
	private static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}

	private static NakedObjectConfiguration getConfiguration() {
		return NakedObjectsContext.getConfiguration();
	}
    

}

// Copyright (c) Naked Objects Group Ltd.
