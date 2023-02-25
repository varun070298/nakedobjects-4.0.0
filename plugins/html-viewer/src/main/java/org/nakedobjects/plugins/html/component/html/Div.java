package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentComposite;


public class Div extends ComponentComposite implements Block {
    private final String className;
    private final String id;
    private final String description;

    public Div(final String className, final String description) {
        this.className = className;
        this.description = description;
        id = null;
    }

    public Div(final String className, final String description, final String id) {
        this.description = description;
        this.className = className;
        this.id = id;
    }

    @Override
    public void write(final PrintWriter writer) {
        super.write(writer);
    }

    @Override
    protected void writeBefore(final PrintWriter writer) {
        writer.print("<div");
        if (className != null) {
            writer.print(" class=\"");
            writer.print(className);
            writer.print("\"");
        }
        if (id != null) {
            writer.print(" id=\"");
            writer.print(id);
            writer.print("\"");
        }
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print(">");
    }

    @Override
    protected void writeAfter(final PrintWriter writer) {
        writer.println("</div>");
    }

    @Override
    public void add(final Component component) {
        super.add(component);
    }

    public void add(final String text) {
        super.add(new Html(text));
    }

}

// Copyright (c) Naked Objects Group Ltd.
