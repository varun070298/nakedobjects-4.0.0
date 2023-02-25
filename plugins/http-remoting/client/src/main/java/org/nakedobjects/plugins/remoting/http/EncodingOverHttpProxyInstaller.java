package org.nakedobjects.plugins.remoting.http;

import org.nakedobjects.plugins.remoting.http.transport.HttpClientTransport;
import org.nakedobjects.remoting.client.ProxyInstallerAbstract;
import org.nakedobjects.remoting.protocol.encoding.EncodingMarshaller;
import org.nakedobjects.remoting.transport.Transport;
import java.util.List;

public class EncodingOverHttpProxyInstaller extends ProxyInstallerAbstract {

    public EncodingOverHttpProxyInstaller() {
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
	protected EncodingMarshaller createMarshaller(Transport transport) {
		return new EncodingMarshaller(getConfiguration(), transport);
	}

    @Override
	protected Transport createTransport() {
		return new HttpClientTransport(getConfiguration());
	}

}
// Copyright (c) Naked Objects Group Ltd.
