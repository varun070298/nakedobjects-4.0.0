package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class PropertyDefaultFacetDerivedFromDefaultedFacet extends FacetAbstract implements PropertyDefaultFacet {

    private final DefaultedFacet typeFacet;
	private final RuntimeContext runtimeContext;

    public PropertyDefaultFacetDerivedFromDefaultedFacet(
    		final DefaultedFacet typeFacet, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(PropertyDefaultFacet.class, holder, false);
        this.typeFacet = typeFacet;
        this.runtimeContext = runtimeContext;
    }

    public NakedObject getDefault(final NakedObject inObject) {
        if (getIdentified() == null) {
			return null;
		}
		Object typeFacetDefault = typeFacet.getDefault();
		if (typeFacetDefault == null) {
			return null;
		}
		return getRuntimeContext().adapterFor(typeFacetDefault);
    }


	///////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ///////////////////////////////////////////////////////

    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}

// Copyright (c) Naked Objects Group Ltd.
