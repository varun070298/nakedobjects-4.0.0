package org.nakedobjects.webapp.monitor;

import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstallerAbstract;
import org.nakedobjects.runtime.web.WebAppSpecification;
import org.nakedobjects.runtime.web.EmbeddedWebViewer;


public class WebServerMonitorInstaller extends NakedObjectsViewerInstallerAbstract {

	public WebServerMonitorInstaller() {
		super("web-monitor");
	}

    @Override
    public NakedObjectsViewer doCreateViewer() {
        return new EmbeddedWebViewer() {
        	@Override
            public WebAppSpecification getWebAppSpecification() {
                WebAppSpecification requirements = new WebAppSpecification();
                requirements.addServletSpecification(MonitorServlet.class, "/monitor/*");
                return requirements;
            }
        };
    }

}

// Copyright (c) Naked Objects Group Ltd.

