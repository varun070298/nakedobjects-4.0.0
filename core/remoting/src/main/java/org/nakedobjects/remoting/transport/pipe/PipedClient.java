package org.nakedobjects.remoting.transport.pipe;

import org.nakedobjects.remoting.client.ClientConnection;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;


public class PipedClient implements ClientConnection {
    private PipedConnection communication;

    public void setConnection(final PipedConnection communication) {
        this.communication = communication;
    }

    public synchronized ResponseEnvelope executeRemotely(final Request request) {
        communication.setRequest(request);
        return communication.getResponse();
    }

    public void init() {}

    public void shutdown() {}
}
// Copyright (c) Naked Objects Group Ltd.
