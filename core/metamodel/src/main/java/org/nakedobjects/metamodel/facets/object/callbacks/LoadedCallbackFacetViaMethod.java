package org.nakedobjects.metamodel.facets.object.callbacks;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class LoadedCallbackFacetViaMethod extends LoadedCallbackFacetAbstract implements ImperativeFacet {

    private final List<Method> methods = new ArrayList<Method>();

    public LoadedCallbackFacetViaMethod(final Method method, final FacetHolder holder) {
        super(holder);
        addMethod(method);
    }

	public void addMethod(Method method) {
		methods.add(method);
	}

    public List<Method> getMethods() {
    	return Collections.unmodifiableList(methods);
    }

	public boolean impliesResolve() {
		return false;
	}

	public boolean impliesObjectChanged() {
		return false;
	}
	
    @Override
    public void invoke(final NakedObject adapter) {
    	NakedObjectInvokeUtils.invoke(methods, adapter);
    }

    @Override
    protected String toStringValues() {
        return "methods=" + methods;
    }

}

// Copyright (c) Naked Objects Group Ltd.
