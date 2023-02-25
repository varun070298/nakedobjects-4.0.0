package org.nakedobjects.remoting.client;

import java.util.List;

import org.nakedobjects.remoting.protocol.ClientMarshaller;
import org.nakedobjects.remoting.protocol.serialize.SerializingMarshaller;
import org.nakedobjects.remoting.transport.Transport;
import org.nakedobjects.remoting.transport.socket.SocketTransport;


public class SerializingOverSocketsProxyInstaller extends ProxyInstallerAbstract {

    public SerializingOverSocketsProxyInstaller() {
		super("serializing-sockets");
	}

	@Override
	protected void addConfigurationResources(List<String> configurationResources) {
		super.addConfigurationResources(configurationResources);
		// TODO: this (small) hack is because we don't load up the Protocol (Marshaller)
		// and Transport using the installers.
		configurationResources.add("protocol.properties");
		configurationResources.add("protocol_serializing.properties");
		configurationResources.add("transport.properties");
		configurationResources.add("transport_sockets.properties");
	}


	@Override
	protected Transport createTransport() {
		return new SocketTransport(getConfiguration());
	}

    @Override
	protected ClientMarshaller createMarshaller(Transport transport) {
		return new SerializingMarshaller(getConfiguration(), transport);
	}
	
}
// Copyright (c) Naked Objects Group Ltd.
