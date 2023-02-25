package org.nakedobjects.remoting.server;

import java.util.List;

import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.NakedObjects;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;

/**
 * Implementation of a {@link NakedObjectsViewer} providing the ability to run
 * from server as a {@link NakedObjects command line} application.
 * 
 * <p>
 * To run, use the <tt>--viewer encoding-sockets</tt> flag. The client-side
 * should run using <tt>--connector encoding-sockets</tt> flag.
 */
public class EncodingOverSocketsViewerInstaller extends
		SocketsViewerInstallerAbstract {

	public EncodingOverSocketsViewerInstaller() {
		super("encoding-sockets");
	}

	@Override
	protected void addConfigurationResources(List<String> configurationResources) {
		super.addConfigurationResources(configurationResources);
		// TODO: this (small) hack is because we don't load up the Protocol (Marshaller)
		// and Transport using the installers.
		configurationResources.add("protocol.properties");
		configurationResources.add("protocol_encoding.properties");
		configurationResources.add("transport.properties");
		configurationResources.add("transport_sockets.properties");
	}

	@Override
	protected SocketsViewerAbstract createSocketsViewer(
			final ObjectEncoderDecoder objectEncoderDecoder) {
		return new EncodingOverSocketsViewer(objectEncoderDecoder);
	}

}

// Copyright (c) Naked Objects Group Ltd.
