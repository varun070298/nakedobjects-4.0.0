package org.nakedobjects.plugins.html.action;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public class LogOut implements Action {
    public void execute(final Request request, final Context context, final Page page) {
    	AuthenticationSession authSession = NakedObjectsContext.getAuthenticationSession();
    	if (authSession != null) {
    		getAuthenticationManager().closeSession(authSession);
    	}
        context.setSession(null); // setSession is probably redundant since now always available via NakedObjectsContext
                                  // can't rely on it being set because Filter may set httpSession 
                                  // (if in exploration mode) rather than ever hitting the LogonServlet 
        context.invalidate();
    }

	private static AuthenticationManager getAuthenticationManager() {
		return NakedObjectsContext.getAuthenticationManager();
	}

    public String name() {
        return "logout";
    }
}

// Copyright (c) Naked Objects Group Ltd.
