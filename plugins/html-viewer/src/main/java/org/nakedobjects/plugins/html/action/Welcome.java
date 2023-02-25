package org.nakedobjects.plugins.html.action;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public class Welcome implements Action {
    public static final String COMMAND = "start";

    public void execute(final Request request, final Context context, final Page page) {
        page.setTitle("NOF Application");

        context.init();

        final ViewPane content = page.getViewPane();
        content.setTitle("Welcome", null);

        String name = AboutNakedObjects.getApplicationName();
        if (name == null) {
            name = AboutNakedObjects.getFrameworkName();
        }
        content.add(context.getComponentFactory().createInlineBlock("message",
                "Welcome to " + name + ", accessed via the Web Viewer", null));
    }

    public String name() {
        return COMMAND;
    }

}

// Copyright (c) Naked Objects Group Ltd.
