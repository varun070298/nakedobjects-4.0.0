package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


public class Submenu implements Component {

    private final String menuName;
    private final Component[] items;

    public Submenu(final String menuName, final Component[] items) {
        this.menuName = menuName;
        this.items = items;
    }

    public void write(final PrintWriter writer) {
        writer.println("<div class=\"submenu-item\">");
        writer.println(menuName);
        for (int j = 0; j < items.length; j++) {
            items[j].write(writer);
        }
        writer.println("</div>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
