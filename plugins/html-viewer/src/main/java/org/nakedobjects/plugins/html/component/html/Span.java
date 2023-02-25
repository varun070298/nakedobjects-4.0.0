package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


public class Span implements Component {
    private final String className;
    private final String value;
    private final String description;

    public Span(final String className, final String value, final String description) {
        this.className = className;
        this.value = value;
        this.description = description;
    }

    public void write(final PrintWriter writer) {
        writer.print("<span class=\"");
        writer.print(className);
        writer.print("\"");
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print(">");
        if (value != null) {
            writer.print(value);
        }
        writer.print("</span>");
    }
}

// Copyright (c) Naked Objects Group Ltd.
