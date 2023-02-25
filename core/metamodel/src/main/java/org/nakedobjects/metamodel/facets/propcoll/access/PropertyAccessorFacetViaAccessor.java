package org.nakedobjects.metamodel.facets.propcoll.access;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class PropertyAccessorFacetViaAccessor extends PropertyAccessorFacetAbstract implements ImperativeFacet {

    private final Method method;

    public PropertyAccessorFacetViaAccessor(
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

	/**
	 * Bytecode cannot automatically call {@link DomainObjectContainer#objectChanged(Object)}
	 * because cannot distinguish whether interacting with accessor to read it or to modify its contents.
	 */
	public boolean impliesObjectChanged() {
		return false;
	}

	@Override
    public Object getProperty(final NakedObject owningAdapter) {
        return NakedObjectInvokeUtils.invoke(method, owningAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }

}
