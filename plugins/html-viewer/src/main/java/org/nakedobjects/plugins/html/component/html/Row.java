package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentComposite;


class Row extends ComponentComposite {

    private static final int TRUNCATE_LENGTH = 18;

    @Override
    protected void write(final PrintWriter writer, final Component component) {
        writer.print("<td>");
        component.write(writer);
        writer.println("</td>");
    }

    public void addCell(final String string, final boolean truncate) {
        String s;
        if (truncate) {
            s = string.substring(0, Math.min(TRUNCATE_LENGTH, string.length()));
            if (string.length() > TRUNCATE_LENGTH) {
                s += "...";
            }
        } else {
            s = string;
        }
        add(new Html(s));
    }

    public void addCell(final Component component) {
        add(component);
    }

}

// Copyright (c) Naked Objects Group Ltd.
