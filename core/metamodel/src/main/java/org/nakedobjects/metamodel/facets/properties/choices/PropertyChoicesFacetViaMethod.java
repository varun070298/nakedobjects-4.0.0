package org.nakedobjects.metamodel.facets.properties.choices;

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


public class PropertyChoicesFacetViaMethod extends PropertyChoicesFacetAbstract implements ImperativeFacet {

    private final Method method;
    private final Class<?> choicesClass;
	private final SpecificationLoader specificationLoader;
	private final RuntimeContext runtimeContext;

    public PropertyChoicesFacetViaMethod(
    		final Method method, 
    		final Class<?> choicesClass, 
    		final FacetHolder holder, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        super(holder);
        this.method = method;
        this.choicesClass = choicesClass;
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
            return null;
        }
        if (options.getClass().isArray()) {
            return ArrayUtils.getObjectAsObjectArray(options);
        }
        final NakedObjectSpecification specification = getSpecificationLoader().loadSpecification(choicesClass);
        return NakedObjectAdapterUtils.getCollectionAsObjectArray(options, specification, getRuntimeContext());
    }

	@Override
    protected String toStringValues() {
        return "method=" + method + ",class=" + choicesClass;
    }


	//////////////////////////////////////////////////////////
	// Dependencies (from constructor)
	//////////////////////////////////////////////////////////
	
	private SpecificationLoader getSpecificationLoader() {
		return specificationLoader;
	}

	private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}

// Copyright (c) Naked Objects Group Ltd.
