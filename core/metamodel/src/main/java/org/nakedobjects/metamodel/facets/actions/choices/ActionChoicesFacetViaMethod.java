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


public class ActionChoicesFacetViaMethod extends ActionChoicesFacetAbstract implements ImperativeFacet {

    private final Method method;
    private final Class<?> choicesType;
	private final RuntimeContext runtimeContext;
	private final SpecificationLoader specificationLoader;

    public ActionChoicesFacetViaMethod(
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
    

    public Object[][] getChoices(final NakedObject owningAdapter) {
        final Object[] options = (Object[]) NakedObjectInvokeUtils.invoke(method, owningAdapter);
        final Object[][] results = new Object[options.length][];
        for (int i = 0; i < results.length; i++) {
            if (options[i] == null) {
                results[i] = null;
            } else if (options[i].getClass().isArray()) {
                results[i] = ArrayUtils.getObjectAsObjectArray(options[i]);
            } else {
                final NakedObjectSpecification specification = getSpecificationLoader().loadSpecification(choicesType);
                results[i] = NakedObjectAdapterUtils.getCollectionAsObjectArray(options[i], specification, getRuntimeContext());
            }
        }
        return results;
    }

    private SpecificationLoader getSpecificationLoader() {
    	return specificationLoader;
    }
    
    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	@Override
    protected String toStringValues() {
        return "method=" + method + ",type=" + choicesType;
    }


}

// Copyright (c) Naked Objects Group Ltd.
