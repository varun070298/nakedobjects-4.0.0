package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetAbstract;
import org.nakedobjects.metamodel.java5.ImperativeFacet;


public class NamedFacetViaMethod extends NamedFacetAbstract implements ImperativeFacet {

    private final Method method;

    public NamedFacetViaMethod(
    		final String value, 
    		final Method method, 
    		final FacetHolder holder) {
        super(value, holder);
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

}

// Copyright (c) Naked Objects Group Ltd.
