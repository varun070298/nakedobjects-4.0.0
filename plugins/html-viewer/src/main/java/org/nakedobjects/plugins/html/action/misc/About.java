package org.nakedobjects.plugins.html.action.misc;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public class About implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final ViewPane content = page.getViewPane();
        content.setTitle("About", null);

        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getApplicationName(), null));
        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getApplicationVersion(), null));
        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getApplicationCopyrightNotice(),
                null));

        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getFrameworkName(), null));
        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getFrameworkVersion(), null));
        content.add(context.getComponentFactory().createInlineBlock("about", AboutNakedObjects.getFrameworkCopyrightNotice(),
                null));
    }

    public String name() {
        return "about";
    }

}

// Copyright (c) Naked Objects Group Ltd.
