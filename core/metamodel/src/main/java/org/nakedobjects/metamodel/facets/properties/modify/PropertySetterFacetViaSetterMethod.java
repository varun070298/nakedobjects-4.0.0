package org.nakedobjects.metamodel.facets.properties.modify;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;


public class PropertySetterFacetViaSetterMethod extends PropertySetterFacetAbstract implements ImperativeFacet {

    private final Method method;

    public PropertySetterFacetViaSetterMethod(
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
		return true;
	}

    public void setProperty(final NakedObject adapter, final NakedObject valueAdapter) {
        NakedObjectInvokeUtils.invoke(method, adapter, valueAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }

}
