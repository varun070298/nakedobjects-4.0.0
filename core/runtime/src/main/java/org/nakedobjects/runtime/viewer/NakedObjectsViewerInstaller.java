package org.nakedobjects.runtime.viewer;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.runtime.installers.InstallerLookupAware;


public interface NakedObjectsViewerInstaller extends Installer, InstallerLookupAware {

	static String TYPE = "viewer";

    NakedObjectsViewer createViewer();
}

// Copyright (c) Naked Objects Group Ltd.
