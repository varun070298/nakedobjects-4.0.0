package org.nakedobjects.metamodel.facets.actions.choices;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ArrayUtils;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.NakedObjectAdapterUtils;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;



public class ActionParameterChoicesFacetViaMethod extends ActionParameterChoicesFacetAbstract implements ImperativeFacet {

    private final Method method;
    private final Class<?> choicesType;
	private final SpecificationLoader specificationLoader;
	private final RuntimeContext runtimeContext;

    public ActionParameterChoicesFacetViaMethod(
    		final Method method, 
    		final Class<?> choicesType, 
    		final FacetHolder holder, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        super(holder);
        this.method = method;
        this.choicesType = choicesType;
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
	

    public Object[] getChoices(final NakedObject owningAdapter) {
        final Object options = NakedObjectInvokeUtils.invoke(method, owningAdapter);
        if (options == null) {
            return new Object[0];
        }
        if (options.getClass().isArray()) {
            return ArrayUtils.getObjectAsObjectArray(options);
        }
        else {
            final NakedObjectSpecification specification = getSpecificationLoader().loadSpecification(choicesType);
            return NakedObjectAdapterUtils.getCollectionAsObjectArray(options, specification, getRuntimeContext());
        }
    }

	@Override
    protected String toStringValues() {
        return "method=" + method + ",type=" + choicesType;
    }


	
	////////////////////////////////////////////////////////////////////
	// Dependencies (from constructor)
	////////////////////////////////////////////////////////////////////

    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	private SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}

}

// Copyright (c) Naked Objects Group Ltd.


// Copyright (c) Naked Objects Group Ltd.
