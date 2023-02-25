package org.nakedobjects.remoting.transport.pipe;

import org.apache.log4j.Logger;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;


public class PipedConnection {
    private static final Logger LOG = Logger.getLogger(PipedConnection.class);
    private Request request;
    private ResponseEnvelope response;
    private RuntimeException exception;

    public synchronized void setRequest(final Request request) {
        this.request = request;
        notify();
    }

    public synchronized Request getRequest() {
        while (request == null) {
            try {
                wait();
            } catch (final InterruptedException e) {
                LOG.error("wait (getRequest) interrupted", e);
            }
        }

        final Request r = request;
        request = null;
        notify();
        return r;
    }

    public synchronized void setResponse(final ResponseEnvelope response) {
        this.response = response;
        notify();
    }

    public synchronized void setException(final RuntimeException exception) {
        this.exception = exception;
        notify();
    }

    public synchronized ResponseEnvelope getResponse() {
        while (response == null && exception == null) {
            try {
                wait();
            } catch (final InterruptedException e) {
                LOG.error("wait (getResponse) interrupted", e);
            }
        }

        if (exception != null) {
            final RuntimeException toThrow = exception;
            exception = null;
            throw toThrow;
        }

        final ResponseEnvelope r = response;
        response = null;
        notify();
        return r;
    }

}
// Copyright (c) Naked Objects Group Ltd.
