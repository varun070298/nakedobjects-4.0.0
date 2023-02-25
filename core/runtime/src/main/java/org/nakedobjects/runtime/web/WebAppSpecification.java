package org.nakedobjects.runtime.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nakedobjects.metamodel.commons.lang.MapUtils;


/**
 * Defines what servlets, mappings etc are required from an embedded web server.
 */
public final class WebAppSpecification {
	
	private Map<String,String> contextParams = new LinkedHashMap<String, String>();
    private List<Class<?>> servletContextListeners = new ArrayList<Class<?>>();
    private List<ServletSpecification> servletSpecifications = new ArrayList<ServletSpecification>();
    private List<FilterSpecification> filterSpecifications = new ArrayList<FilterSpecification>();
    private List<String> resourcePaths = new ArrayList<String>();
    private List<String> welcomeFiles = new ArrayList<String>();
	private String logHint;

    

    /////////////////////////////////////////////////////////////
    // Context Params
    /////////////////////////////////////////////////////////////

	public void addContextParams(String... contextParams) {
		this.contextParams = MapUtils.asMap(contextParams);
	}
	
	public Map<String, String> getContextParams() {
		return Collections.unmodifiableMap(contextParams);
	}

	
    /////////////////////////////////////////////////////////////
    // Servlet Context Listeners
    /////////////////////////////////////////////////////////////
    
    public void addServletContextListener(Class<?> servletContextListenerClass) {
        servletContextListeners.add(servletContextListenerClass);        
    }
    
    public List<Class<?>> getServletContextListeners() {
        return servletContextListeners;
    }


    /////////////////////////////////////////////////////////////
    // Servlet Mappings
    /////////////////////////////////////////////////////////////
    
    public void addServletSpecification(Class<?> servletClass, String... pathSpecs) {
        servletSpecifications.add(new ServletSpecification(servletClass, pathSpecs));        
    }
    
    public void addServletSpecification(Class<?> servletClass, Map<String,String> initParams, String... pathSpecs) {
        servletSpecifications.add(new ServletSpecification(servletClass, initParams, pathSpecs));        
    }
    
    public List<ServletSpecification> getServletSpecifications() {
        return servletSpecifications;
    }

    /////////////////////////////////////////////////////////////
    // Filter Mappings
    /////////////////////////////////////////////////////////////

    public void addFilterSpecification(Class<?> filterClass, String... pathSpecs) {
        filterSpecifications.add(new FilterSpecification(filterClass, pathSpecs));
    }

    public void addFilterSpecification(Class<?> filterClass, Map<String,String> initParams, String... pathSpecs) {
        filterSpecifications.add(new FilterSpecification(filterClass, initParams, pathSpecs));
    }

    public List<FilterSpecification> getFilterSpecifications() {
        return filterSpecifications;
    }
    
    /////////////////////////////////////////////////////////////
    // Resources
    /////////////////////////////////////////////////////////////

    public void addResourcePath(String path) {
        resourcePaths.add(path);
    }
    public List<String> getResourcePaths() {
        return resourcePaths;
    }


    /////////////////////////////////////////////////////////////
    // Welcome Files
    /////////////////////////////////////////////////////////////

    public void addWelcomeFile(String path) {
        welcomeFiles.add(path);
    }
    
    public List<String> getWelcomeFiles() {
        return welcomeFiles;
    }

    
    /////////////////////////////////////////////////////////////
    // Candifloss
    /////////////////////////////////////////////////////////////

	public String getLogHint() {
		return logHint;
	}

	public void setLogHint(String logHint) {
		this.logHint = logHint;
	}

}

// Copyright (c) Naked Objects Group Ltd.
