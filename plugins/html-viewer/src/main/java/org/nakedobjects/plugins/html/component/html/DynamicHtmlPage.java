package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.DebugPane;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;


public class DynamicHtmlPage extends AbstractHtmlPage implements Page {
    private Component crumbs;
    private DebugPane debugPane;
    private final Block navigation = new Div(null, "navigation");
    private final ViewPane viewPane = new ViewDiv();

    public DynamicHtmlPage(final String styleSheet, final String header, final String footer) {
        super(styleSheet, header, footer);
    }

    public Block getNavigation() {
        return navigation;
    }

    public ViewPane getViewPane() {
        return viewPane;
    }

    public void setCrumbs(final Component crumbs) {
        this.crumbs = crumbs;
    }

    public void setDebug(final DebugPane debugPane) {
        this.debugPane = debugPane;
    }

    @Override
    protected void writeContent(final PrintWriter writer) {
        if (debugPane != null) {
            debugPane.write(writer);
        } else {
            writer.println();
            writer.println("<div id=\"body\">");
            navigation.write(writer);
            if (crumbs != null) {
                crumbs.write(writer);
            }
            viewPane.write(writer);
            writer.println();
            writer.println("</div>");
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
