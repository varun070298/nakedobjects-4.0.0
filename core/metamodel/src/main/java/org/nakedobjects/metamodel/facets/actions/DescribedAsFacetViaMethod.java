package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacetAbstract;
import org.nakedobjects.metamodel.java5.ImperativeFacet;


public class DescribedAsFacetViaMethod extends DescribedAsFacetAbstract implements ImperativeFacet {

    private final Method method;

    public DescribedAsFacetViaMethod(
    		final String value, 
    		final Method method, 
    		final FacetHolder holder) {
        super(value, holder);
        this.method = method;
    }

    public List<Method> getMethods() {
    	return Collections.singletonList(method);
    }


	public boolean impliesResolve() {
		return false;
	}

	public boolean impliesObjectChanged() {
		return false;
	}

}

// Copyright (c) Naked Objects Group Ltd.
