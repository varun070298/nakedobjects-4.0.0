package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.html.component.DebugPane;


public class HtmlDebug implements DebugPane {
    private static final String SPACES = "                                    ";
    private final StringBuffer debug = new StringBuffer();
    private int indent;

    public void addSection(final String title) {
        if (debug.length() > 0) {
            appendln("</pre>");
        }
        appendln("<h2>");
        appendln(title);
        appendln("</h2><pre>");
    }

    public void appendln(final String text) {
        debug.append(SPACES.substring(0, indent * 3));
        debug.append(text);
        debug.append("\n");
    }

    public void write(final PrintWriter writer) {
        if (debug.length() > 0) {
            writer.print(debug.toString());
            writer.println("</pre>");
        }
    }

    public void indent() {
        indent++;
    }

    public void unindent() {
        if (indent == 0) {
            throw new NakedObjectException();
        }
        indent--;
    }
}

// Copyright (c) Naked Objects Group Ltd.
