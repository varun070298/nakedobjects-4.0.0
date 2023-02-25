package org.nakedobjects.remoting.protocol;

import java.io.IOException;

import org.nakedobjects.remoting.exchange.Request;


public interface ClientMarshaller extends Marshaller {
    
    public void connect() throws IOException;
    public Object request(final Request request) throws IOException;
    public void disconnect();
    

}
