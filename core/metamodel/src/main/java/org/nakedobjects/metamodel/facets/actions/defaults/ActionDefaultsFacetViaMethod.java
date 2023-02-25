package org.nakedobjects.metamodel.facets.actions.defaults;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacetViaMethod;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class ActionDefaultsFacetViaMethod extends ActionDefaultsFacetAbstract implements ImperativeFacet {

    private final Method method;
    
    @SuppressWarnings("unused")
	private final Method actionMethod;

    public ActionDefaultsFacetViaMethod(
    		final Method method, 
    		final FacetHolder holder) {
        super(holder, false);
        this.method = method;
        final Facet actionInvocationFacet = holder.getFacet(ActionInvocationFacet.class);
        if (actionInvocationFacet instanceof ActionInvocationFacetViaMethod) {
            final ActionInvocationFacetViaMethod facetViaMethod = (ActionInvocationFacetViaMethod) actionInvocationFacet;
            actionMethod = facetViaMethod.getMethods().get(0);
        } else {
            actionMethod = null;
        }
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
    

    public Object[] getDefaults(final NakedObject owningAdapter) {
        final Object[] defaults = (Object[]) NakedObjectInvokeUtils.invoke(method, owningAdapter);
        return defaults;

        // TODO fix the setting up actionMethod so that we can check the types.

        /*
         * NakedObject[] array = new NakedObject[defaults.length]; // if (actionMethod != null) {
         * NakedObjectReflector reflector = NakedObjectsContext.getReflector(); // Class[] parameterTypes =
         * actionMethod.getParameterTypes(); for (int i = 0; i < array.length; i++) { // Class parameterType =
         * parameterTypes[i]; // NakedObjectSpecification paramSpecification =
         * reflector.loadSpecification(parameterType); // if (paramSpecification.isObject()) { array[i] =
         * PersistorUtil.createAdapter(defaults[i]); / } else { throw new
         * UnknownTypeException(paramSpecification.getFullName()); } / } //} return (NakedObject[]) array;
         */
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }


}

// Copyright (c) Naked Objects Group Ltd.
