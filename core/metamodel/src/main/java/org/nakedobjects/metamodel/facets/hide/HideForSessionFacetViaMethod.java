package org.nakedobjects.metamodel.facets.hide;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.authentication.AuthenticationSessionUtils;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.InvokeUtils;


public class HideForSessionFacetViaMethod extends HideForSessionFacetAbstract implements ImperativeFacet {

    private final Method method;

    public HideForSessionFacetViaMethod(
    		final Method method, 
    		final FacetHolder holder) {
        super(holder);
        this.method = method;
    }

    /**
     * Returns a singleton list of the {@link Method} provided in the constructor. 
     */
    public List<Method> getMethods() {
    	return Collections.singletonList(method);
    }

	public boolean impliesResolve() {
		return true;
	}

	public boolean impliesObjectChanged() {
		return false;
	}

    /**
     * Will only check provided that a {@link AuthenticationSession} has been provided.
     */
    public String hiddenReason(final AuthenticationSession session) {
        if (session == null) {
            return null;
        }
        final int len = method.getParameterTypes().length;
        final Object[] parameters = new Object[len];
        parameters[0] = AuthenticationSessionUtils.createUserMemento(session);
        // TODO: need to change to pick up as non-static rather than static
        final Boolean isHidden = (Boolean) InvokeUtils.invokeStatic(method, parameters);
        return isHidden.booleanValue() ? "Hidden" : null;
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }

}

// Copyright (c) Naked Objects Group Ltd.
