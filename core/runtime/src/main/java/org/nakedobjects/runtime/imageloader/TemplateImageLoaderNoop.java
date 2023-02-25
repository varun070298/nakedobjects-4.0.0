package org.nakedobjects.runtime.imageloader;

import org.nakedobjects.metamodel.commons.component.Noop;

/**
 * No-op implementation, for tests.
 */
public class TemplateImageLoaderNoop implements TemplateImageLoader, Noop {

    public TemplateImage getTemplateImage(String name) {
        return null;
    }
    public void init() {}
    public void shutdown() {}

}


// Copyright (c) Naked Objects Group Ltd.
