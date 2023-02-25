package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.ComponentAbstract;


public class BreadCrumbs extends ComponentAbstract {
    private final String[] names;
    private final boolean[] isLinked;

    public BreadCrumbs(final String[] names, final boolean[] isLinked) {
        this.names = names;
        this.isLinked = isLinked;
    }

    public void write(final PrintWriter writer) {
        writer.println("<div id=\"context\">");

        final int length = names.length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                writer.print("<span class=\"separator\"> &gt; </span>");
            }
            if (isLinked[i]) {
                writer.print("<a class=\"linked\" href=\"context.app?id=");
                writer.print(i);
                writer.print("\">");
                writer.print(names[i]);
                writer.print("</a>");
            } else if (!(i == length - 1 && names[i] == null)) {
                writer.print("<span class=\"disabled\">");
                writer.print(names[i]);
                writer.print("</span>");
            }
        }

        writer.print("</div>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
