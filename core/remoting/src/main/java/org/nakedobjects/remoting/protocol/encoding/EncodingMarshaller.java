package org.nakedobjects.remoting.protocol.encoding;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DebugDataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DebugDataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.protocol.MarshallerAbstract;
import org.nakedobjects.remoting.protocol.ProtocolConstants;
import org.nakedobjects.remoting.transport.Transport;


public class EncodingMarshaller extends MarshallerAbstract {
    
    public EncodingMarshaller(
    		final NakedObjectConfiguration configuration,
			final Transport transport) {
		super(configuration, transport);
	}

	private static final Logger LOG = Logger.getLogger(EncodingMarshaller.class);

    private static enum As {
    	ENCODABLE(0) {
			@Override
			public void writeObject(DataOutputExtended output, Object object) throws IOException {
		    	writeTo(output);
		        output.writeEncodable(object);
			}
			@Override
			public <T> T readObject(DataInputExtended input, Class<T> cls) throws IOException {
				// not quite symmetrical with write; the byte has already been read from stream
				// to determine which As instance to delegate to
		        return input.readEncodable(cls);
			}
		},
    	SERIALIZABLE(1) {
			@Override
			public void writeObject(DataOutputExtended output, Object object) throws IOException {
		    	writeTo(output);
		        output.writeSerializable(object);
			}
			@Override
			public <T> T readObject(DataInputExtended input, Class<T> cls) throws IOException {
				// not quite symmetrical with write; the byte has already been read from stream
				// to determine which As instance to delegate to
		        return input.readSerializable(cls);
			}
		};
    	static Map<Integer, As> cache = new HashMap<Integer, As>();
    	static {
    		for(As as: values()) {
    			cache.put(as.idx, as);
    		}
    	}
    	private final int idx;
    	private As(int idx) {
    		this.idx = idx;
    	}
    	static As get(int idx) {
    		return cache.get(idx); 
    	}
		public static As readFrom(DataInputExtended input) throws IOException {
			return get(input.readByte());
		}
		public void writeTo(DataOutputExtended output) throws IOException {
			output.writeByte(idx);
		}
		public abstract void writeObject(DataOutputExtended output, Object object) throws IOException;
		public abstract <T> T readObject(DataInputExtended input, Class<T> cls) throws IOException;
    }
    

    //////////////////////////////////////////////////////////////////
    // Properties
    //////////////////////////////////////////////////////////////////

	private boolean debugging;
	
    private DataInputExtended input;
    private DataOutputExtended output;



    //////////////////////////////////////////////////////////////////
    // Common to both ClientMarshaller and ServerMarshaller
    //////////////////////////////////////////////////////////////////

    @Override
    public void init() {
    	super.init();
        debugging = getConfiguration().getBoolean(ProtocolConstants.DEBUGGING_KEY, ProtocolConstants.DEBUGGING_DEFAULT);
        if (LOG.isInfoEnabled()) {
        	LOG.info("debugging=" + debugging);
        }
    }


    //////////////////////////////////////////////////////////////////
    // Common to both ClientMarshaller and ServerMarshaller
    //////////////////////////////////////////////////////////////////

    @Override
    public void connect() throws IOException {
    	super.connect();
    	
    	this.input =  new DataInputStreamExtended(getTransport().getInputStream());
    	this.output =  new DataOutputStreamExtended(getTransport().getOutputStream());
    	
    	if(debugging) {
	    	this.input = new DebugDataInputExtended(input);
	        this.output = new DebugDataOutputExtended(output);
    	}
    }

    private void writeToOutput(final Object object) throws IOException {
    	// expect to be either Encodable or Serializable, prefer the former.
    	if (object instanceof Encodable) {
    		As.ENCODABLE.writeObject(output, object);
    	} else {
    		As.SERIALIZABLE.writeObject(output, object);
    	}
        output.flush();
    }

    private <T> T readFromInput(Class<T> cls) throws IOException {
    	As as = As.readFrom(input);
    	return as.readObject(input, cls);
    }

    
    //////////////////////////////////////////////////////////////////
    // ClientMarshaller impl
    //////////////////////////////////////////////////////////////////
    

    public Object request(final Request request) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sending request " + request);
        }
        try {
            writeToOutput(request);
        } catch (final SocketException e) {
            reconnect();
            writeToOutput(request);
        }
        final Object object = readFromInput(Object.class);
        if (LOG.isDebugEnabled()) {
            LOG.debug("response received: " + object);
        }
        if (!isKeepAlive()) {
            disconnect();
        }
        return object;
    }

    
    //////////////////////////////////////////////////////////////////
    // ServerMarshaller impl
    //////////////////////////////////////////////////////////////////

    public Request readRequest() throws IOException {
        final Request request = readFromInput(Request.class);
        if (LOG.isDebugEnabled()) {
            LOG.debug("request received: " + request);
        }
        return request;
    }

    public void sendResponse(final Object response) throws IOException {
        if (LOG.isDebugEnabled()) {
		    LOG.debug("send response: " + response);
		}
		writeToOutput(response);
    }

    public void sendError(final NakedObjectException exception) throws IOException {
        if (LOG.isDebugEnabled()) {
		    LOG.debug("send error: " + exception);
		}
		writeToOutput(exception);
    }

}
// Copyright (c) Naked Objects Group Ltd.
