package org.nakedobjects.metamodel.facets.actions.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.ReflectiveActionException;
import org.nakedobjects.metamodel.util.InvokeUtils;


public class ActionInvocationFacetViaMethod extends ActionInvocationFacetAbstract implements ImperativeFacet {

    private final static Logger LOG = Logger.getLogger(ActionInvocationFacetViaMethod.class);

    private final Method method;
    private final int paramCount;
    private final NakedObjectSpecification onType;
    private final NakedObjectSpecification returnType;

	private final RuntimeContext runtimeContext;

    public ActionInvocationFacetViaMethod(
            final Method method,
            final NakedObjectSpecification onType,
            final NakedObjectSpecification returnType,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        super(holder);
        this.method = method;
        this.paramCount = method.getParameterTypes().length;
        this.onType = onType;
        this.returnType = returnType;
        this.runtimeContext = runtimeContext;
    }

    /**
     * Returns a singleton list of the {@link Method} provided in the constructor. 
     */
    public List<Method> getMethods() {
    	return Collections.singletonList(method);
    }

    public NakedObjectSpecification getReturnType() {
        return returnType;
    }

    public NakedObjectSpecification getOnType() {
        return onType;
    }

    public NakedObject invoke(final NakedObject inObject, final NakedObject[] parameters) {
        if (parameters.length != paramCount) {
            LOG.error(method + " requires " + paramCount + " parameters, not " + parameters.length);
        }

        try {
            final Object[] executionParameters = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                executionParameters[i] = unwrap(parameters[i]);
            }

            final Object object = unwrap(inObject);
            final Object result = method.invoke(object, executionParameters);
            LOG.debug(" action result " + result);
            if (result == null) {
                return null;
            }

            final NakedObject adapter = getRuntimeContext().adapterFor(result);
            final TypeOfFacet typeOfFacet = getFacetHolder().getFacet(TypeOfFacet.class);
            adapter.setTypeOfFacet(typeOfFacet);
            return adapter;

        } catch (final InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                throw new ReflectiveActionException("IllegalStateException thrown while executing " + method + " "
                        + e.getTargetException().getMessage(), e.getTargetException());
            } else {
                InvokeUtils.invocationException("Exception executing " + method, e);
                return null;
            }
        } catch (final IllegalAccessException e) {
            throw new ReflectiveActionException("Illegal access of " + method, e);
        }
    }


	private static Object unwrap(final NakedObject adapter) {
	    return adapter == null ? null : adapter.getObject();
	}

	
	public boolean impliesResolve() {
		return true;
	}

	public boolean impliesObjectChanged() {
		return false;
	}
	

	@Override
    protected String toStringValues() {
        return "method=" + method;
    }
    
    
    ///////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ///////////////////////////////////////////////////////////

    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}

// Copyright (c) Naked Objects Group Ltd.
