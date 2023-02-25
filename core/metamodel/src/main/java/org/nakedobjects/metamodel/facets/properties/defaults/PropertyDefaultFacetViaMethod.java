package org.nakedobjects.metamodel.facets.properties.defaults;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class PropertyDefaultFacetViaMethod extends PropertyDefaultFacetAbstract implements ImperativeFacet {

    private final Method method;
	private final SpecificationLoader specificationLoader;
	private final RuntimeContext runtimeContext;

    public PropertyDefaultFacetViaMethod(
    		final Method method, 
    		final FacetHolder holder, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        super(holder);
        this.method = method;
        this.specificationLoader = specificationLoader;
        this.runtimeContext = runtimeContext;
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

    public NakedObject getDefault(final NakedObject owningAdapter) {
        final Object result = NakedObjectInvokeUtils.invoke(method, owningAdapter);
        return createAdapter(method.getReturnType(), result);
    }

    
    private NakedObject createAdapter(final Class<?> type, final Object object) {
	    final NakedObjectSpecification specification = getSpecificationLoader().loadSpecification(type);
	    if (specification.isObject()) {
	        return getRuntimeContext().adapterFor(object);
	    } else {
	        throw new UnknownTypeException("not an object, is this a collection?");
	    }
	}


	@Override
    protected String toStringValues() {
        return "method=" + method;
    }


    // //////////////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    // //////////////////////////////////////////////////////////////////


    private SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}

    protected RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
    

}

// Copyright (c) Naked Objects Group Ltd.
