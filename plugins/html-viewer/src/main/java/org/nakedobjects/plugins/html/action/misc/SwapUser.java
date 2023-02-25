package org.nakedobjects.plugins.html.action.misc;

import java.rmi.server.ExportException;
import java.util.StringTokenizer;

import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationAuthenticatorConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public class SwapUser implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final ViewPane content = page.getViewPane();
        content.setTitle("Swap Exploration User", null);

        // TODO pick out users from the perspectives, but only show when in exploration mode
        final String users = NakedObjectsContext.getConfiguration().getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
        if (users != null) {
            final StringTokenizer st = new StringTokenizer(users, ",");
            if (st.countTokens() > 0) {
                while (st.hasMoreTokens()) {
                    final String token = st.nextToken();
                    int end = token.indexOf(':');
                    if (end == -1) {
                        end = token.length();
                    }
                    final String name = token.substring(0, end).trim();

                    content.add(context.getComponentFactory().createUserSwap(name));
                }
            }
        }

        // TODO find user list and interate through them
        /*
         * content.add(context.getFactory().createInlineBlock("title", AboutNakedObjects.getApplicationName(),
         * null)); content.add(context.getFactory().createInlineBlock("title",
         * AboutNakedObjects.getApplicationVersion(), null));
         * content.add(context.getFactory().createInlineBlock("title",
         * AboutNakedObjects.getApplicationCopyrightNotice(), null));
         * 
         * content.add(context.getFactory().createInlineBlock("title", AboutNakedObjects.getFrameworkName(),
         * null)); content.add(context.getFactory().createInlineBlock("title",
         * AboutNakedObjects.getFrameworkVersion(), null));
         * content.add(context.getFactory().createInlineBlock("title",
         * AboutNakedObjects.getFrameworkCopyrightNotice(), null));
         */
    }

    public String name() {
        return "swapuser";
    }
}

// Copyright (c) Naked Objects Group Ltd.
