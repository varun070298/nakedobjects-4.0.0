package org.nakedobjects.runtime.web;

import java.util.Map;


public final class FilterSpecification extends AbstractServletOrFilterMapping {

    /**
     * @param filterClass - should extend <tt>javax.servlet.Filter</tt>
     */
    public FilterSpecification(
    		final Class<?> filterClass, 
    		final Map<String, String> initParams,
    		final String... mappings) {
		super(filterClass, initParams, mappings);
	}

    /**
     * @param filterClass - should extend <tt>javax.servlet.Filter</tt>
     */
	public FilterSpecification(
			final Class<?> filterClass, 
			final String... pathSpecs) {
		super(filterClass, pathSpecs);
	}

	public Class<?> getFilterClass() {
        return getServletOrFilterClass();
    }

}

// Copyright (c) Naked Objects Group Ltd.
