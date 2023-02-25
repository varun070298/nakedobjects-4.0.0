package org.nakedobjects.remoting.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.threads.ThreadRunner;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.facade.ServerFacadeLogger;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.ProfilingInputStream;
import org.nakedobjects.remoting.transport.ProfilingOutputStream;
import org.nakedobjects.remoting.transport.socket.SocketTransportConstants;
import org.nakedobjects.remoting.transport.socket.server.Worker;
import org.nakedobjects.remoting.transport.socket.server.WorkerPool;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.system.internal.monitor.HttpServerMonitor;
import org.nakedobjects.runtime.system.internal.monitor.SocketServerMonitor;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerAbstract;


public abstract class SocketsViewerAbstract extends NakedObjectsViewerAbstract implements DebugInfo {

    private static final Logger LOG = Logger.getLogger(SocketsViewerAbstract.class);

    final ObjectEncoderDecoder encoderDecoder;

    private ServerSocket socket;
    private WorkerPool workerPool;

    private Boolean shutdown = Boolean.FALSE;

    public SocketsViewerAbstract(final ObjectEncoderDecoder encoderDecoder) {
        this.encoderDecoder = encoderDecoder;
    }

    // ////////////////////////////////////////////////////////////////
    // init
    // ////////////////////////////////////////////////////////////////

    /**
     * TODO: generalize so can listen on multi-homed hosts using new Socket(port, 2, address);
     */
    public void init() {
        super.init();

        final int port = getConfiguration().getInteger(SocketsViewerConstants.SERVER_PORT, SocketTransportConstants.PORT_DEFAULT);

        workerPool = new WorkerPool(5);

        final ServerFacadeImpl serverFacade = new ServerFacadeImpl(getAuthenticationManager());
        serverFacade.setEncoder(encoderDecoder);

        final ServerFacade sd = new ServerFacadeLogger(encoderDecoder, serverFacade);
        sd.init();

        socket = newServerSocket(port);

        LOG.info("listening for connection on " + socket);
        acceptAndHandleRequests(sd);

        shutdownListener();
    }

    private ServerSocket newServerSocket(final int port) {
        try {
            ServerSocket socket = new ServerSocket(port);
            LOG.info("nof listener started on " + socket);
            socket.setSoTimeout(1000);
            return socket;
        } catch (final IOException e) {
            throw new NakedObjectException(e);
        } finally {
            shutdown(socket);
        }
    }

    private void acceptAndHandleRequests(final ServerFacade sd) {
        while (!isShutdown()) {
            try {
                final Socket clientSocket = socket.accept();
                LOG.info("connection accepted from " + clientSocket.getInetAddress());
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                if (isDebugging()) {
                    inputStream = new ProfilingInputStream(inputStream);
                    outputStream = new ProfilingOutputStream(outputStream);
                }

                final ServerConnection connection = createServerConnection(inputStream, outputStream, sd);
                // spawnConnectionThread(connection, sd);

                final Worker worker = workerPool.getWorker();
                worker.setIncomingConnection(connection);
                // worker.start();

                // main thread accepts new connection - have a connection - therefore implicitly a request
                // get worker; associate with connection

                // receive request
                // process request
                // return worker to pool and drop connection
                // or
                // repeat from 3 (if KEEP_ALIVE)

                // main thread accepts new connection
                // adds connection to to queue
                // repeats, waiting for new connection

                // (one of many) consumer picks up connection from queue
                // wait for request
                // process request
                // consumer returns connection
                // repeat from top

            } catch (final InterruptedIOException ignore) {
                // timeout - check at top of loop
                continue;
            } catch (final IOException e) {
                LOG.warn("connection exception", e);
                continue;
            } catch (final ConnectionException e) {
                LOG.warn("connection exception", e);
            }
        }
    }

    // ////////////////////////////////////////////////////////////////
    // shutdown
    // ////////////////////////////////////////////////////////////////

    public void shutdown() {
        LOG.info("stopping listener");
        synchronized (shutdown) {
            shutdown = Boolean.TRUE;
        }
    }

    // ////////////////////////////////////////////////////////////////
    // shutdown
    // ////////////////////////////////////////////////////////////////

    private void shutdownListener() {
        shutdown(socket);
        socket = null;

        shutdown(workerPool);
        workerPool = null;
    }

    private void shutdown(final ServerSocket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
            LOG.info("socket closed");
        } catch (final IOException e) {
            LOG.error("Failed to close listening socket", e);
        }
    }

    private void shutdown(final WorkerPool workerPool) {
        if (workerPool == null) {
            return;
        }
        workerPool.shutdown();
        LOG.info("worker pool stopped");
    }

    protected boolean isShutdown() {
        synchronized (shutdown) {
            return shutdown.booleanValue();
        }
    }

    // ///////////////////////////////////////////////////////
    // hooks
    // ///////////////////////////////////////////////////////

    /**
     * Hook method.
     */
    protected abstract ServerConnection createServerConnection(
            final InputStream input,
            final OutputStream output,
            final ServerFacade serverFacade);

    // ////////////////////////////////////////////////////////////////
    // debugging
    // ////////////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.appendln("Listener on", socket.toString());
        debug.appendln("Workers", workerPool);
        workerPool.debug(debug);
    }

    public String debugTitle() {
        return "Server Listener";
    }

    private boolean isDebugging() {
        return true;
    }

    // ///////////////////////////////////////////////////////////////
    //
    // ///////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    private void startMonitors(final NakedObjectsSystem system) {
        if (system.getDeploymentType().shouldMonitor()) {
            Runnable serverMonitorRunnable = new Runnable() {
                public void run() {
                    final SocketServerMonitor serverMonitor = new SocketServerMonitor();
                    serverMonitor.setTarget(system);
                    serverMonitor.listen();
                }
            };

            new ThreadRunner().startThread(serverMonitorRunnable, "Monitor-1");

            Runnable httpServerMonitorRunnable = new Runnable() {
                public void run() {
                    final HttpServerMonitor httpServerMonitor = new HttpServerMonitor();
                    httpServerMonitor.setTarget(system);
                    httpServerMonitor.listen();
                }
            };
            new ThreadRunner().startThread(httpServerMonitorRunnable, "Monitor-2");
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
