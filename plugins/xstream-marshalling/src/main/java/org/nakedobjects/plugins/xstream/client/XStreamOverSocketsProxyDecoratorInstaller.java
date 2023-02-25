package org.nakedobjects.plugins.xstream.client;

import java.util.List;

import org.nakedobjects.plugins.xstream.shared.XStreamMarshaller;
import org.nakedobjects.remoting.client.ProxyInstallerAbstract;
import org.nakedobjects.remoting.protocol.ClientMarshaller;
import org.nakedobjects.remoting.transport.Transport;
import org.nakedobjects.remoting.transport.socket.SocketTransport;


public class XStreamOverSocketsProxyDecoratorInstaller extends ProxyInstallerAbstract {

    public XStreamOverSocketsProxyDecoratorInstaller() {
		super("xstream-sockets");
	}

	@Override
	protected void addConfigurationResources(List<String> configurationResources) {
		super.addConfigurationResources(configurationResources);
		// TODO: this (small) hack is because we don't load up the Protocol (Marshaller)
		// and Transport using the installers.
		configurationResources.add("protocol.properties");
		configurationResources.add("protocol_xstream.properties");
		configurationResources.add("transport.properties");
		configurationResources.add("transport_sockets.properties");
	}

    @Override
    protected Transport createTransport() {
    	return new SocketTransport(getConfiguration());
    }
    
    @Override
	protected ClientMarshaller createMarshaller(Transport transport) {
		return new XStreamMarshaller(getConfiguration(), transport);
	}

}

// Copyright (c) Naked Objects Group Ltd.
