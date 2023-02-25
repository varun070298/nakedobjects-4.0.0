package org.nakedobjects.runtime.viewer;

import org.nakedobjects.metamodel.commons.component.Noop;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.web.WebAppSpecification;

public class NakedObjectsViewerNoop implements NakedObjectsViewer, Noop {

    public void init() {}

    public void shutdown() {}

	public void setInstallerLookup(InstallerLookup installerLookup) {
	}

	public void setConfigurationBuilder(ConfigurationBuilder configurationLoader) {
	}

	public boolean bootstrapsSystem() {
		return false;
	}

	public WebAppSpecification getWebAppSpecification() {
	    return null;
	}
}


// Copyright (c) Naked Objects Group Ltd.
