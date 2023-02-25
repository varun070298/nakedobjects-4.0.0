package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


class ActionComponent implements Component {
    private final String objectId;
    private final String name;
    private final String description;
    private final String field;
    private final String action;
    private final String elementId;

    public ActionComponent(
            final String action,
            final String name,
            final String description,
            final String objectId,
            final String elementId,
            final String field) {
        this.action = action;
        this.name = name;
        this.description = description;
        this.objectId = objectId;
        this.elementId = elementId;
        this.field = field;
    }

    public void write(final PrintWriter writer) {
        writer.print("<div class=\"action-button\">");
        writer.print("<a href=\"");
        writer.print(action);
        writer.print(".app?id=");
        writer.print(objectId);
        if (field != null) {
            writer.print("&amp;field=");
            writer.print(field);
        }
        if (elementId != null) {
            writer.print("&amp;element=");
            writer.print(elementId);
        }
        writer.print("\" title=\"");
        writer.print(description);
        writer.print("\"> ");
        writer.print(name);
        writer.print("</a>");
        writer.println("</div>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
