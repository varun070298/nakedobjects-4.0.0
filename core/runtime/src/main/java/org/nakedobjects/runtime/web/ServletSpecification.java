package org.nakedobjects.runtime.web;

import java.util.Map;



public final class ServletSpecification extends AbstractServletOrFilterMapping {
	

    /**
     * @param servletClass - should extend <tt>javax.servlet.Servlet</tt>
     */
    public ServletSpecification(
    		final Class<?> servletClass, 
    		final Map<String, String> initParams,
    		final String... mappings) {
		super(servletClass, initParams, mappings);
	}

    /**
     * @param servletClass - should extend <tt>javax.servlet.Servlet</tt>
     */
	public ServletSpecification(
			final Class<?> servletClass, 
			final String... pathSpecs) {
		super(servletClass, pathSpecs);
	}

	public Class<?> getServletClass() {
        return getServletOrFilterClass();
    }

}

// Copyright (c) Naked Objects Group Ltd.
