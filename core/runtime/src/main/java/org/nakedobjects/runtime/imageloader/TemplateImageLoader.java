package org.nakedobjects.runtime.imageloader;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;


/**
 * This interface defines how template images are found by name.
 */
public interface TemplateImageLoader extends ApplicationScopedComponent {
    
    TemplateImage getTemplateImage(final String name);
}

// Copyright (c) Naked Objects Group Ltd.
