package org.nakedobjects.plugins.dnd.bootstrap;

import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstallerAbstract;


public class DndViewerInstaller extends NakedObjectsViewerInstallerAbstract {

	public DndViewerInstaller() {
		super("dnd");
	}

	@Override
	public NakedObjectsViewer doCreateViewer() {
        return new DndViewer();
    }


}

// Copyright (c) Naked Objects Group Ltd.
