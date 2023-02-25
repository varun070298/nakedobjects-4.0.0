package org.nakedobjects.remoting.protocol;

import java.io.IOException;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.remoting.exchange.Request;

public interface ServerMarshaller extends Marshaller {

    public Request readRequest() throws IOException;

    public void sendResponse(Object response) throws IOException;

    public void sendError(NakedObjectException exception) throws IOException;

}
