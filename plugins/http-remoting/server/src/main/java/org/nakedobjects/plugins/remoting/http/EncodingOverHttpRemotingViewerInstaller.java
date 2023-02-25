package org.nakedobjects.plugins.remoting.http;

import java.util.List;

import org.nakedobjects.runtime.NakedObjects;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstallerAbstract;
import org.nakedobjects.runtime.web.EmbeddedWebViewer;
import org.nakedobjects.runtime.web.WebAppSpecification;

/**
 * Convenience implementation of a {@link NakedObjectsViewer} providing the
 * ability to run a Jetty web server configured for http remoting from the
 * {@link NakedObjects command line}.
 * 
 * <p>
 * To run, use the <tt>--viewer encoding-http</tt> flag. The client-side should
 * run using <tt>--connector encoding-http</tt> flag.
 * 
 * <p>
 * In a production deployment the configuration represented by the
 * {@link WebAppSpecification} would be specified in the <tt>web.xml<tt> file.
 */
public class EncodingOverHttpRemotingViewerInstaller extends
		NakedObjectsViewerInstallerAbstract {

	private static final String REMOTING_SERVLET_MAPPED = "/remoting.svc";
	
	public EncodingOverHttpRemotingViewerInstaller() {
		super("encoding-http");
	}

	@Override
	protected void addConfigurationResources(List<String> configurationResources) {
		super.addConfigurationResources(configurationResources);
		// TODO: this (small) hack is because we don't load up the Protocol (Marshaller)
		// and Transport using the installers.
		configurationResources.add("protocol.properties");
		configurationResources.add("protocol_encoding.properties");
		configurationResources.add("transport.properties");
		configurationResources.add("transport_http.properties");
	}


	@Override
	public NakedObjectsViewer doCreateViewer() {
		return new EmbeddedWebViewer() {
			public WebAppSpecification getWebAppSpecification() {

				WebAppSpecification webAppSpec = new WebAppSpecification();
				webAppSpec.addServletSpecification(
						EncodingOverHttpRemotingServlet.class,
						REMOTING_SERVLET_MAPPED);

				return webAppSpec;
			}
		};
	}

}

// Copyright (c) Naked Objects Group Ltd.

