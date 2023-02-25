package org.nakedobjects.runtime.system;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugSelection;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;

public interface NakedObjectsSystem extends DebugSelection, ApplicationScopedComponent {

    DeploymentType getDeploymentType();
    
    LogonFixture getLogonFixture();

    /**
     * Populated after {@link #init()}.
     */
    public NakedObjectSessionFactory getSessionFactory();

	/**
     * Returns a <i>snapshot</i> of the {@link NakedObjectConfiguration configuration} (although
     * once the {@link NakedObjectsSystem} is completely initialized, will effectively be immutable).
     */
	NakedObjectConfiguration getConfiguration();

}


// Copyright (c) Naked Objects Group Ltd.
