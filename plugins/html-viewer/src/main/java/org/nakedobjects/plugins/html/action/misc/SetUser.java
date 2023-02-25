package org.nakedobjects.plugins.html.action.misc;

import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.authentication.standard.exploration.MultiUserExplorationSession;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class SetUser implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final String name = request.getName();
        final MultiUserExplorationSession session = (MultiUserExplorationSession) NakedObjectsContext.getAuthenticationSession();
        session.setCurrentSession(name);

        final ViewPane content = page.getViewPane();
        content.setTitle("Exploration User changed to " + name, null);

    }

    public String name() {
        return "setuser";
    }
}

// Copyright (c) Naked Objects Group Ltd.
