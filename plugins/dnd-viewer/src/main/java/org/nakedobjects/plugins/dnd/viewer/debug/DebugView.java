package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.util.Dump;


public class DebugView implements DebugInfo {
    private final View view;

    public DebugView(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        debug.append(view.getView());
        debug.blankLine();
        debug.blankLine();

        // display details
        debug.appendTitle("VIEW");

        view.debug(debug);
        debug.appendln();

        // content
        final Content content = view.getContent();
        debug.appendTitle("CONTENT");
        if (content != null) {
            String type = content.getClass().getName();
            type = type.substring(type.lastIndexOf('.') + 1);
            debug.appendln("Content", type);
            content.debugDetails(debug);

            debug.indent();
            debug.appendln("Icon name", content.getIconName());
            debug.appendln("Icon ", content.getIconPicture(32));
            debug.appendln("Window title", content.windowTitle());
            debug.appendln("Persistable", content.isPersistable());
            debug.appendln("Object", content.isObject());
            debug.appendln("Collection", content.isCollection());

            debug.appendln("Parseable", content.isTextParseable());
            debug.unindent();
        } else {
            debug.appendln("Content", "none");
        }
        debug.blankLine();

        if (content instanceof ObjectContent) {
            final NakedObject object = ((ObjectContent) content).getObject();
            dumpObject(object, debug);
            debug.blankLine();
            dumpSpecification(object, debug);
            debug.blankLine();
            dumpGraph(object, debug);

        } else if (content instanceof CollectionContent) {
            final NakedObject collection = ((CollectionContent) content).getCollection();
            debug.blankLine();
            dumpObject(collection, debug);
            dumpSpecification(collection, debug);
            debug.blankLine();
            dumpGraph(collection, debug);
        }

        debug.append("\n\nDRAWING\n");
        debug.append("------\n");
        view.draw(new DebugCanvas(debug, new Bounds(view.getBounds())));
    }

    public String debugTitle() {
        return "Debug: " + view + view == null ? "" : ("/" + view.getContent());
    }

    public void dumpGraph(final NakedObject object, final DebugString info) {
        if (object != null) {
            info.appendTitle("GRAPH");
            Dump.graph(object, info, NakedObjectsContext.getAuthenticationSession());
        }
    }

    public void dumpObject(final NakedObject object, final DebugString info) {
        if (object != null) {
            info.appendTitle("OBJECT");
            Dump.adapter(object, info);
        }
    }

    private void dumpSpecification(final NakedObject object, final DebugString info) {
        if (object != null) {
            info.appendTitle("SPECIFICATION");
            Dump.specification(object, info);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
