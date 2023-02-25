package org.nakedobjects.plugins.html.action.view;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public class ServiceView extends ObjectViewAbstract {
    @Override
    protected void doExecute(final Context context, final ViewPane content, final NakedObject object, final String field) {
        context.setObjectCrumb(object);
    }

    public String name() {
        return Request.SERVICE_COMMAND;
    }

}

// Copyright (c) Naked Objects Group Ltd.
