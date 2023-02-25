package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.ComponentAbstract;


public class Heading extends ComponentAbstract {
    private final String title;
    private final int level;

    public Heading(final String title) {
        this(title, 1);
    }

    public Heading(final String title, final int level) {
        this.title = title;
        this.level = level;
    }

    public void write(final PrintWriter writer) {
        writeTag(writer, "h" + level);
        writer.print(title);
        writer.print("</h");
        writer.print(level);
        writer.println(">");
    }

}

// Copyright (c) Naked Objects Group Ltd.
