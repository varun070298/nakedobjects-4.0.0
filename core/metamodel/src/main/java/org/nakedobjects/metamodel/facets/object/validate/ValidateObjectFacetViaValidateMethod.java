package org.nakedobjects.metamodel.facets.object.validate;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class ValidateObjectFacetViaValidateMethod extends ValidateObjectFacetAbstract implements ImperativeFacet {

    private final Method method;

    public ValidateObjectFacetViaValidateMethod(final Method method, final FacetHolder holder) {
        super(holder);
        this.method = method;
    }

    public List<Method> getMethods() {
    	return Collections.singletonList(method);
    }

	public boolean impliesResolve() {
		return true;
	}

	public boolean impliesObjectChanged() {
		return false;
	}

    public String invalidReason(final NakedObject owningAdapter) {
        return (String) NakedObjectInvokeUtils.invoke(method, owningAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }

}

// Copyright (c) Naked Objects Group Ltd.
