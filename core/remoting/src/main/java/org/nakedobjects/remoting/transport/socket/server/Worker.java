package org.nakedobjects.remoting.transport.socket.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.server.ServerConnection;
import org.nakedobjects.remoting.server.ServerConnectionHandler;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.internal.monitor.Monitor;

public class Worker implements Runnable {
    private static final Logger LOG = Logger.getLogger(Worker.class);
    private static int nextId = 1;
    
    private final WorkerPool poolToReturnTo;
    private final int id = nextId++;

    private boolean running = true;
    
    private ServerConnection connection;
	private ServerConnectionHandler serverDelegate;

    public Worker(final WorkerPool pool) {
        this.poolToReturnTo = pool;
    }

    public synchronized void gracefulStop() {
        running = false;
    }

    public boolean isAvailable() {
        return connection == null;
    }

    ///////////////////////////////////////////////////////
    // run, setIncomingConnection, wait
    ///////////////////////////////////////////////////////

    public synchronized void run() {
        while (running) {
        	waitForIncomingConnection();
            if (connection == null) {
            	break;
            }

            handleRequest();
        }
        LOG.info("Stopping: " + toString());
    }

    /**
     * @see #waitForIncomingConnection()
     */
    public synchronized void setIncomingConnection(final ServerConnection connection) {
        this.connection = connection;
        this.serverDelegate = new ServerConnectionHandler(connection);
        notify();
    }

    /**
     * @see #setIncomingConnection(ServerConnection)
     */
	private void waitForIncomingConnection() {
		while (connection == null) {
		    try {
		        wait();
		    } catch (final InterruptedException e) {
		        if (!running) {
		            LOG.info("Request to stop : " + toString());
		            break;
		        }
		    }
		}
	}


    ///////////////////////////////////////////////////////
    // handleRequest
    ///////////////////////////////////////////////////////

	private void handleRequest() {
		try {
			serverDelegate.handleRequest();
		} catch (final SocketException e) {
		    LOG.info("shutting down receiver (" + e + ")");
		} catch (final IOException e) {
		    LOG.info("connection exception; closing connection", e);
		} finally {
		    end();
		}
	}

    private void end() {
    	serverDelegate = null;
        connection = null;
        poolToReturnTo.returnWorker(this);
    }

    
    
    ///////////////////////////////////////////////////////
    // Debug
    ///////////////////////////////////////////////////////
    
    public void debug(final DebugString debug) {
    	serverDelegate.debug(debug);
    }

    
    ///////////////////////////////////////////////////////
    // toString
    ///////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "Worker#" + id;
    }
}

// Copyright (c) Naked Objects Group Ltd.
