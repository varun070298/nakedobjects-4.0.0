package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.DebugPane;
import org.nakedobjects.plugins.html.component.ViewPane;


public class LogonFormPage extends AbstractHtmlPage {
    private final String user;
    private final String password;

    public LogonFormPage(
            final String styleSheet,
            final String header,
            final String footer,
            final String user,
            final String password) {
        super(styleSheet, header, footer);
        this.user = user;
        this.password = password;
    }

    @Override
    protected void writeContent(final PrintWriter writer) {
        writer.println("<div id=\"view\">");
        writer.println("<div class=\"header\">");
        if (user.equals("")) {
            writer.println("<span class=\"header-text\">Please enter a user name and password.</span>");
        } else {
            writer.println("<span class=\"header-text\">Please enter a valid user name and password.</span>");
        }
        writer.println("</div>");
        writer.println("<FORM ACTION=\"logon.app\" METHOD=\"post\">");
        writer.println("<div id=\"content\">");
        writer.println("<div class=\"field\"><span class=\"label\">User name</span>"
                + "<span class=\"separator\">: </span><INPUT NAME=\"username\" value=\"" + user + "\"></DIV>");
        writer.println("<div class=\"field\"><span class=\"label\">Password</span>"
                + "<span class=\"separator\">: </span><INPUT TYPE=\"password\" NAME=\"password\" value=\"" + password
                + "\"></DIV>");
        writer.println("<div class=\"action-button\"><INPUT TYPE=\"submit\" VALUE=\"Log in\" NAME=\"Log in\"></div>");
        writer.println("</div>");
        writer.println("</FORM>");
        writer.println("</div>");

    }

    public Block getNavigation() {
        return null;
    }

    public ViewPane getViewPane() {
        return null;
    }

    public void setCrumbs(final Component component) {}

    public void setDebug(final DebugPane debugPane) {}

}

// Copyright (c) Naked Objects Group Ltd.
