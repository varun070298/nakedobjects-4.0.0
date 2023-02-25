package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentComposite;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.image.ImageLookup;


public class ViewDiv extends ComponentComposite implements ViewPane {
    private String iconName;
    private String objectId;
    private Component[] menu = new Component[0];
    private String title;
    private List<String> messages = new ArrayList<String>();
    private List<String> warnings = new ArrayList<String>();
    private String description;

    public void setIconName(final String iconName) {
        this.iconName = iconName;
    }

    public void setLink(final String objectId) {
        this.objectId = objectId;
    }

    public void setMenu(final Component[] menu) {
        this.menu = menu;
    }

    public void setTitle(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public void setWarningsAndMessages(final List<String> messages, final List<String> warnings) {
        this.messages.addAll(messages);
        this.warnings.addAll(warnings);
    }

    @Override
    protected void writeAfter(final PrintWriter writer) {
        writer.println("</div>");
        writer.println("</div>");
    }

    @Override
    protected void writeBefore(final PrintWriter writer) {
        writer.println("<div id=\"view\">");
        writeMessages(writer);
        writeHeader(writer);
        writeMenu(writer);
        writer.println("<div id=\"content\">");
    }

    private void writeMessages(final PrintWriter writer) {
        if (warnings.size() > 0 || messages.size() > 0) {
            writer.print("<div class=\"message-header\">");
            for (String warning: warnings) {
                writer.print("<div class=\"warning\">");
                writer.print(warning);
                writer.println("</div>");
            }
            for (String message: messages) {
                writer.print("<div class=\"message\">");
                writer.print(message);
                writer.println("</div>");
            }
            writer.print("</div>");
        }
    }

    private void writeMenu(final PrintWriter writer) {
        writer.println("<div id=\"menu\">");
        writer.println("<h3>Actions</h3>");
        for (int j = 0; j < menu.length; j++) {
            menu[j].write(writer);
        }
        writer.println("</div>");
    }

    private void writeHeader(final PrintWriter writer) {
        writer.print("<div class=\"header\"");
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print(">");
        if (objectId != null) {
            writer.print("<a href=\"object.app?id=");
            writer.print(objectId);
            writer.print("\">");
        }
        if (iconName != null) {
            writer.print("<span class=\"header-icon\"><img src=\"");
            writer.print(ImageLookup.image(iconName));
            writer.print("\" alt=\"icon\" /></span>");
        }
        writer.print("<span class=\"header-text\"");
        writer.print(">");
        writer.print(title);
        writer.println("</span>");
        if (objectId != null) {
            writer.print("</a>");
        }
        writer.println("</div>");
    }
}

// Copyright (c) Naked Objects Group Ltd.
