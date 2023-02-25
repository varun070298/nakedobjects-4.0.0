package org.nakedobjects.runtime.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public abstract class EmbeddedWebServerAbstract implements EmbeddedWebServer {

    @SuppressWarnings("unused")
    private final static Logger LOG = Logger.getLogger(EmbeddedWebServerAbstract.class);

    private List<WebAppSpecification> specifications = new ArrayList<WebAppSpecification>();

    // ///////////////////////////////////////////////////////
    // WebApp Specifications
    // ///////////////////////////////////////////////////////

    /**
     * Must be called prior to {@link #init() initialization}.
     */
    public void addWebAppSpecification(WebAppSpecification specification) {
        specifications.add(specification);
    }

    protected List<WebAppSpecification> getSpecifications() {
        return specifications;
    }

    // ///////////////////////////////////////////////////////
    // init, shutdown
    // ///////////////////////////////////////////////////////

    public void init() {
    // does nothing
    }

    public void shutdown() {
    // does nothing
    }

    // ///////////////////////////////////////////////////////
    // Dependencies (from context)
    // ///////////////////////////////////////////////////////

    protected static NakedObjectConfiguration getConfiguration() {
        return NakedObjectsContext.getConfiguration();
    }

}

// Copyright (c) Naked Objects Group Ltd.
