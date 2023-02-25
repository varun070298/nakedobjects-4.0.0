package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;


public class DebugContent implements DebugInfo {
    private final View view;

    public DebugContent(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        final Content content = view.getContent();
        if (content != null) {
            String type = content.getClass().getName();
            type = type.substring(type.lastIndexOf('.') + 1);
            debug.appendln("Content", type);

            debug.indent();

            content.debugDetails(debug);

            debug.appendln("Icon name", content.getIconName());
            debug.appendln("Icon ", content.getIconPicture(32));
            debug.appendln("Window title", content.windowTitle());

            debug.appendln("Object", content.isObject());
            debug.appendln("Collection", content.isCollection());

            debug.appendln("Text Parseable", content.isTextParseable());

            debug.unindent();
        } else {
            debug.appendln("Content", "none");
        }
        debug.blankLine();
    }

    public String debugTitle() {
        return "Content";
    }
}
// Copyright (c) Naked Objects Group Ltd.
