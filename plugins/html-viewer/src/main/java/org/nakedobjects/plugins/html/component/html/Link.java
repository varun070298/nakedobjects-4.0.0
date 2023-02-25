package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


public class Link implements Component {
    private final String link;
    private final String name;
    private final String description;

    public Link(final String link, final String name, final String description) {
        this.link = link;
        this.name = name;
        this.description = description;
    }

    public void write(final PrintWriter writer) {
        writer.print("<a class=\"link\" title=\"" + description + "\" href=\"" + link + ".app\">" + name + "</a>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
