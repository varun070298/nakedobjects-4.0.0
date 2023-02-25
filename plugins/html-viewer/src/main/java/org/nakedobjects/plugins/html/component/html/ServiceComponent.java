package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.image.ImageLookup;


public class ServiceComponent implements Component {

    private final String id;
    private final String name;
    private final String iconName;

    public ServiceComponent(final String id, final String name, final String iconName) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
    }

    public void write(final PrintWriter writer) {
        writer.print("<div class=\"item\">");

        writer.print("<a href=\"");
        writer.print("serviceOption.app?id=");
        writer.print(id);
        writer.print("\"><img src=\"");
        writer.print(ImageLookup.image(iconName));
        writer.print("\" alt=\"service\" />");
        writer.print(name);
        writer.print("</a>");

        writer.println("</div>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
