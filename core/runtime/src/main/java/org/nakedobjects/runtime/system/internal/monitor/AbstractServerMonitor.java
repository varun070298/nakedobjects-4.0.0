package org.nakedobjects.runtime.system.internal.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.services.InitialisationException;
import org.nakedobjects.runtime.system.NakedObjectsSystem;


public abstract class AbstractServerMonitor {
    private static final Logger LOG = Logger.getLogger(AbstractServerMonitor.class);
    private static final String ADDRESS = ConfigurationConstants.ROOT + "monitor.address";
    private boolean acceptConnection = true;

    public void listen() {
        final String hostAddress = NakedObjectsContext.getConfiguration().getString(ADDRESS);
        InetAddress address;
        try {
            address = hostAddress == null ? null : InetAddress.getByName(hostAddress);
            final int port = getPort();
            final ServerSocket serverSocket = new ServerSocket(port, 2, address);
            serverSocket.setSoTimeout(5000);
            LOG.info("waiting for monitor connection on " + serverSocket);
            while (acceptConnection) {
                Socket client = null;
                try {
                    client = serverSocket.accept();
                    LOG.info("client connection on " + client);
                } catch (final SocketTimeoutException ignore) {
                    // ignore
                    continue;
                } catch (final IOException e) {
                    LOG.error("request failed", e);
                    continue;
                }
                try {
                    handleRequest(client);
                } catch (final Exception e) {
                    LOG.error("request failed", e);
                }
            }
        } catch (final UnknownHostException e) {
            throw new InitialisationException(e);
        } catch (final IOException e) {
            throw new InitialisationException(e);
        }
    }

    protected abstract int getPort();

    private void handleRequest(final Socket socket) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        String request;
        do {
            request = reader.readLine();
        } while (handleRequest(writer, request));
        writer.close();
        reader.close();
    }

    public abstract void setTarget(NakedObjectsSystem system);

    public void shutdown() {
        acceptConnection = false;
    }

    protected abstract boolean handleRequest(PrintWriter writer, String request) throws IOException;
}

// Copyright (c) Naked Objects Group Ltd.
