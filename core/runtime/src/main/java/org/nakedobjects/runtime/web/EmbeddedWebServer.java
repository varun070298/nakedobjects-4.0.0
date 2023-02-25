package org.nakedobjects.runtime.web;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;

public interface EmbeddedWebServer extends ApplicationScopedComponent {
    
    void addWebAppSpecification(WebAppSpecification webContainerRequirements);

}


// Copyright (c) Naked Objects Group Ltd.
