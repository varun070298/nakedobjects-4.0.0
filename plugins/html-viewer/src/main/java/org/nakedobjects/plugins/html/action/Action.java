package org.nakedobjects.plugins.html.action;

import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public interface Action {

    void execute(Request request, Context context, Page page);

    String name();

}

// Copyright (c) Naked Objects Group Ltd.
