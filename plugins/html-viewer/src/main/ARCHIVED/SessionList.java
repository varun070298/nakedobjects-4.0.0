package org.nakedobjects.plugins.htmlviewer.webapp;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.servlet.internal.SessionAccess;


public class SessionList implements HttpSessionListener {
    private static final String NOF_SESSION_ATTRIBUTE = "nof-context";

    public void sessionCreated(final HttpSessionEvent event) {
        final HttpSession session = event.getSession();
        SessionAccess.addSession(session);
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
        final HttpSession session = event.getSession();
        SessionAccess.removeSession(session);

        final Context context = (Context) session.getAttribute(NOF_SESSION_ATTRIBUTE);
        final AuthenticationSession nofSession = context.getSession();
        if (nofSession != null) {
            SessionAccess.logoffUser(nofSession);
        }

    }

}

// Copyright (c) Naked Objects Group Ltd.
