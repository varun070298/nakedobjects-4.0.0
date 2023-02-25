package org.nakedobjects.remoting.transport;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;

public abstract class TransportAbstract implements Transport {

	private final NakedObjectConfiguration configuration;
    
    public TransportAbstract(NakedObjectConfiguration configuration) {
    	this.configuration = configuration;
    }
    
	
	//////////////////////////////////////////////////////////////////////
	// Dependencies (injected in constructor)
	//////////////////////////////////////////////////////////////////////
	
	public NakedObjectConfiguration getConfiguration() {
		return configuration;
	}
}
