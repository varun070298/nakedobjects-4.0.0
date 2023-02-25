package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.html.component.Component;


public class MenuItem implements Component {
    private final String actionId;
    private final String actionName;
    private final String objectId;
    private final String actionDescription;
    private final String reasonDisabled;
    private final boolean takesParameters;
    private final NakedObjectActionType type;

    public MenuItem(
            final String actionId,
            final String actionName,
            final String actionDescription,
            final String reasonDisabled,
            final NakedObjectActionType type,
            final boolean takesParameters,
            final String objectId) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionDescription = actionDescription;
        this.reasonDisabled = reasonDisabled;
        this.type = type;
        this.takesParameters = takesParameters;
        this.objectId = objectId;
    }

    public void write(final PrintWriter writer) {
        writer.print("<div class=\"menu-item\">");
        if (isEmpty(reasonDisabled)) {
            writeEnabledLink(writer);
        } else {
            writeDisabledLink(writer);
        }
        writer.println("</div>");
    }

    private boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

    private void writeDisabledLink(final PrintWriter writer) {
        writer.print("<div class=\"disabled\" title=\"");
        writer.print(reasonDisabled);
        writer.print("\">");
        writer.print(actionName);
        if (takesParameters) {
            writer.print(". . .");
        }
        writer.print("</div>");
    }

    private void writeEnabledLink(final PrintWriter writer) {
        writer.print("<a title=\"");
        writer.print(actionDescription);
        writer.print("\" href=\"");
        writer.print("method.app?id=");
        writer.print(objectId);
        writer.print("&amp;action=");
        writer.print(actionId);
        writer.print("\">");
        if (type == NakedObjectActionConstants.EXPLORATION) {
            writer.print("[");
            writer.print(actionName);
            writer.print("]");
        } else {
            writer.print(actionName);
        }
        if (takesParameters) {
            writer.print(". . .");
        }
        writer.print("</a>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
