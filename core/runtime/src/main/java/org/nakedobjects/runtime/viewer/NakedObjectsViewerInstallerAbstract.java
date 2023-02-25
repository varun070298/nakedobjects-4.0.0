package org.nakedobjects.runtime.viewer;

import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.installers.InstallerLookup;


public abstract class NakedObjectsViewerInstallerAbstract extends InstallerAbstract implements NakedObjectsViewerInstaller {

	private InstallerLookup installerLookup;

	public NakedObjectsViewerInstallerAbstract(String name) {
		super(NakedObjectsViewerInstaller.TYPE, name);
	}
	
    public NakedObjectsViewer createViewer() {
        return injectDependenciesInto(doCreateViewer());
    }
    
    /**
     * Subclasses should override (or else override {@link #createViewer()} if they need to do anything more elaborate.
     */
    protected NakedObjectsViewer doCreateViewer() {
    	return null;
    }

    protected <T> T injectDependenciesInto(T candidate) {
    	return installerLookup.injectDependenciesInto(candidate);
    }
    
	public void setInstallerLookup(InstallerLookup installerLookup) {
		this.installerLookup = installerLookup;
	}

}

// Copyright (c) Naked Objects Group Ltd.
