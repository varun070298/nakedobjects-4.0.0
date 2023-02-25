package org.nakedobjects.remoting.server;

import java.io.IOException;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.ServerMarshaller;

public class ServerConnectionDefault implements ServerConnection {

    private final ServerFacade server;
    private ServerMarshaller serverMarshaller;

    public ServerConnectionDefault(final ServerFacade server, final ServerMarshaller serverMarshaller) {
        this.server = server;
        this.serverMarshaller = serverMarshaller; 
    }

    public ServerFacade getServerFacade() {
        return server;
    }

    protected ServerMarshaller getServerMarshaller() {
        return serverMarshaller;
    }

    public Request readRequest() throws IOException {
        return serverMarshaller.readRequest();
    }

    public void sendResponse(Object response) throws IOException {
        serverMarshaller.sendResponse(response);
    }

    public void sendError(NakedObjectException exception) throws IOException {
        serverMarshaller.sendError(exception);
    }


}
// Copyright (c) Naked Objects Group Ltd.
