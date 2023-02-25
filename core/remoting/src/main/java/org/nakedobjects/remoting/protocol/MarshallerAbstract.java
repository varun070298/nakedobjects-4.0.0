package org.nakedobjects.remoting.protocol;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.transport.Transport;


public abstract class MarshallerAbstract implements ClientMarshaller, ServerMarshaller {

	private static final Logger LOG = Logger.getLogger(MarshallerAbstract.class);

	private final NakedObjectConfiguration configuration;
	private final Transport transport;
	
	private boolean keepAlive;

    
    public MarshallerAbstract(
    		final NakedObjectConfiguration configuration, final Transport transport) {
		this.configuration = configuration;
		this.transport = transport;
	}

    //////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////

    public void init() {
    	transport.init();
        keepAlive = getConfiguration().getBoolean(ProtocolConstants.KEEPALIVE_KEY, ProtocolConstants.KEEPALIVE_DEFAULT);
        if (LOG.isInfoEnabled()) {
        	LOG.info("keepAlive=" + keepAlive);
        }
    }

	public void shutdown() {
    	transport.disconnect();
    	transport.shutdown();
	}


    //////////////////////////////////////////////////////////
    // Settings
    //////////////////////////////////////////////////////////
	
	public boolean isKeepAlive() {
		return keepAlive;
	}

	
    //////////////////////////////////////////////////////////
    // connect
    //////////////////////////////////////////////////////////
	
	public void connect() throws IOException {
		transport.connect();
	}
	
	public void disconnect() {
		transport.disconnect();
	}
	
	/**
	 * Not API.  Whether reconnects are performed depends on the
	 * marshaller/protocol.
	 */
    protected void reconnect() throws IOException {
        disconnect();
        connect();
    }


    //////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    //////////////////////////////////////////////////////////

	public NakedObjectConfiguration getConfiguration() {
		return configuration;
	}
    
	public Transport getTransport() {
		return transport;
	}
	
}
