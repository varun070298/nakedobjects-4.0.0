package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentComposite;


class TableHeader extends ComponentComposite {

    @Override
    protected void write(final PrintWriter writer, final Component component) {
        writer.print("<th>");
        component.write(writer);
        writer.println("</th>");
    }

    public void addHeader(final String string) {
        add(new Html(string));
    }
}

// Copyright (c) Naked Objects Group Ltd.
