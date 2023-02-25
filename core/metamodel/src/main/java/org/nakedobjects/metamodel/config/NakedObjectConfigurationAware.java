package org.nakedobjects.metamodel.config;


/**
 * Allows components (eg facet factories) to indicate that they have a dependency on
 * {@link NakedObjectConfiguration}.
 */
public interface NakedObjectConfigurationAware {

    /**
     * Inject the {@link NakedObjectConfiguration} into the component.
     */
    void setNakedObjectConfiguration(NakedObjectConfiguration configuration);
}
// Copyright (c) Naked Objects Group Ltd.
