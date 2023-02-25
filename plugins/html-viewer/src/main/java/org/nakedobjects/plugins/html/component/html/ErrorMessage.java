package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


public class ErrorMessage implements Component {

    private final Exception e;
    private final boolean isDebug;

    public ErrorMessage(final Exception e, final boolean isDebug) {
        this.e = e;
        this.isDebug = isDebug;
    }

    public void write(final PrintWriter writer) {
        writer.println("<div class=\"error\">");
        writer.println(e.getMessage());
        writer.println("</div>");
        if (isDebug) {
            writer.println("<pre class=\"error-trace\">");
            e.printStackTrace(writer);
            writer.println("</pre>");
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
