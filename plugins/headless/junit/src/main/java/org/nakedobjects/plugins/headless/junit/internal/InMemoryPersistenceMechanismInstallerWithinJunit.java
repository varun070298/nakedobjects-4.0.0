package org.nakedobjects.plugins.headless.junit.internal;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.headless.viewer.DomainObjectContainerHeadlessViewer;
import org.nakedobjects.runtime.objectstore.inmemory.InMemoryPersistenceMechanismInstaller;

public class InMemoryPersistenceMechanismInstallerWithinJunit extends InMemoryPersistenceMechanismInstaller {

    /**
     * Returns a {@link DomainObjectContainerHeadlessViewer}.
     */
    @Override
    protected DomainObjectContainer createContainer(
    		final NakedObjectConfiguration configuration) {
        return new DomainObjectContainerHeadlessViewer();
    }

}


// Copyright (c) Naked Objects Group Ltd.
