package org.nakedobjects.webapp.monitor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nakedobjects.runtime.system.internal.monitor.MonitorListenerImpl;


public class MonitorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MonitorListenerImpl monitor;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final String queryString = request.getQueryString();
        final String query = queryString == null ? "Overview" : URLDecoder.decode(queryString, "UTF-8");
        response.setContentType("text/html");
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream()));
        if (query.equals("Sessions")) {
            writer.println("<HTML><HEAD><TITLE>NOF System Monitor - " + "Sessions" + "</TITLE></HEAD>");
            writer.println("<BODY>");

            writer.println("<h1>" + "Sessions" + "</h1>");
            writer.println("<pre>");
            writer.println(listSessions());
            writer.println("</pre>");
            writer.println("</BODY></HTML>");
        } else {
            monitor.writeHtmlPage(query, writer);
        }
        writer.flush();
    }

    private static String listSessions() {
        final StringBuffer str = new StringBuffer();
        /*
        final Iterator<?> it = SessionAccess.getSessions().iterator();
        while (it.hasNext()) {
            final HttpSession session = (HttpSession) it.next();
            final String id = session.getId();
            str.append(id);
            str.append(" \t");

            final long creationTime = session.getCreationTime();
            str.append(new Date(creationTime));
            str.append(" \t");

            final long lastAccessedTime = session.getLastAccessedTime();
            str.append(new Date(lastAccessedTime));
            str.append(" \t");

            final AuthenticationSession nofSession = (AuthenticationSession) session.getAttribute("NOF_SESSION_ATTRIBUTE");
            if (nofSession != null) {
                str.append(nofSession.getUserName());
            }

            str.append("\n");
        }
        */
        return str.toString();
    }

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        monitor = new MonitorListenerImpl();
    }
}

// Copyright (c) Naked Objects Group Ltd.
