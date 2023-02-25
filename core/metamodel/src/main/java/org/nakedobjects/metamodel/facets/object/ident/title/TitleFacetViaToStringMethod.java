package org.nakedobjects.metamodel.facets.object.ident.title;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;


public class TitleFacetViaToStringMethod extends TitleFacetAbstract implements ImperativeFacet {

    private final Method method;

    public TitleFacetViaToStringMethod(
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

    public String title(final NakedObject object) {
        return object.getObject().toString();
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

// Copyright (c) Naked Objects Group Ltd.
