package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.image.ImageLookup;
import org.nakedobjects.plugins.html.request.Request;



public class ObjectIcon implements Component {
    private final NakedObject element;
    private final String id;
    private final String style;
    private final String description;

    public ObjectIcon(final NakedObject element, final String description, final String id, final String style) {
        this.element = element;
        this.description = description;
        this.id = id;
        this.style = style;
    }

    public void write(final PrintWriter writer) {
        writer.print("<div class=\"");
        writer.print(style);
        writer.print("\"");
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print(">");

        writer.print("<a href=\"");
        writer.print(Request.OBJECT_COMMAND + ".app?id=");
        writer.print(id);
        writer.print("\"><img src=\"");
        writer.print(ImageLookup.image(element));
        writer.print("\" alt=\"");
        final String singularName = element.getSpecification().getSingularName();
        writer.print(singularName);
        writer.print("\"");
        writer.print("/>");
        writer.print(element.titleString());
        writer.print("</a>");

        writer.println("</div>");

    }

}

// Copyright (c) Naked Objects Group Ltd.
