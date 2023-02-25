package org.nakedobjects.plugins.html.action;

import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public class ChangeContext implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final int id = Integer.valueOf(request.getObjectId()).intValue();
        request.forward(context.changeContext(id));
    }

    public String name() {
        return "context";
    }

}

// Copyright (c) Naked Objects Group Ltd.
