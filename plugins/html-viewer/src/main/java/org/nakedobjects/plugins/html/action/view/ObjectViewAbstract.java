package org.nakedobjects.plugins.html.action.view;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.action.view.util.MenuUtil;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public abstract class ObjectViewAbstract implements Action {

    public final void execute(final Request request, final Context context, final Page page) {
        final String idString = request.getObjectId();
        final NakedObject adapter = context.getMappedObject(idString);
        if (adapter == null) {
            throw new ActionException("No such object: " + idString);
        }
        
        NakedObjectsContext.getPersistenceSession().resolveImmediately(adapter);

        page.setTitle(adapter.titleString());

        final ViewPane content = page.getViewPane();
        content.setWarningsAndMessages(context.getMessages(), context.getWarnings());
        content.setTitle(adapter.titleString(), adapter.getSpecification().getDescription());
        content.setIconName(adapter.getIconName());

        if (addObjectToHistory()) {
            context.addObjectToHistory(idString);
        }

        context.purge();

        content.setMenu(MenuUtil.menu(adapter, idString, context));

        String iconName = adapter.getIconName();
        if (iconName == null) {
            iconName = adapter.getSpecification().getShortName();
        }

        content.setIconName(iconName);

        final String field = request.getProperty();
        doExecute(context, content, adapter, field);

        context.clearMessagesAndWarnings();
    }

    protected void doExecute(final Context context, final ViewPane content, final NakedObject object, final String field) {}

    protected boolean addObjectToHistory() {
        return false;
    }
}

// Copyright (c) Naked Objects Group Ltd.
