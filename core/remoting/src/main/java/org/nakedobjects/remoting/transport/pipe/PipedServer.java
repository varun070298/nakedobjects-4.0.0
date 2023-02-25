package org.nakedobjects.remoting.transport.pipe;

import org.apache.log4j.Logger;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.facade.ServerFacade;



public class PipedServer {
    private static final Logger LOG = Logger.getLogger(PipedServer.class);
    private ServerFacade facade;
    private PipedConnection communication;

    public synchronized void run() {
        while (true) {
            final Request request = communication.getRequest();
            LOG.debug("client request: " + request);
            try {
                request.execute(facade);
                final ResponseEnvelope response = new ResponseEnvelope(request);
                LOG.debug("server response: " + response);
                communication.setResponse(response);
            } catch (final RuntimeException e) {
                communication.setException(e);
            } catch (final Exception e) {
                LOG.error("failure during request", e);
            }
        }
    }

    public void setConnection(final PipedConnection communication) {
        this.communication = communication;
    }

    public void setFacade(final ServerFacade facade) {
        this.facade = facade;
    }
}
// Copyright (c) Naked Objects Group Ltd.
