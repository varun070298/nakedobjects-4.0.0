package org.nakedobjects.webserver.embedded.jetty;

import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebServer;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebViewer;

/**
 * Not to be confused with {@link NakedObjectsViewerInstaller}, this installer
 * is for a component to <i>host</i> any {@link EmbeddedWebViewer} implementations.
 */
public class JettyEmbeddedWebServerInstaller extends InstallerAbstract implements EmbeddedWebServerInstaller {

    public JettyEmbeddedWebServerInstaller() {
		super(EmbeddedWebServerInstaller.TYPE, "jetty");
	}

    public EmbeddedWebServer createEmbeddedWebServer() {
        return new EmbeddedWebServerJetty();
    }

}


// Copyright (c) Naked Objects Group Ltd.
