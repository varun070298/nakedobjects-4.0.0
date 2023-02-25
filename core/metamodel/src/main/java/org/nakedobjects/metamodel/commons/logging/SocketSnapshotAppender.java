package org.nakedobjects.metamodel.commons.logging;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.TriggeringEventEvaluator;


public class SocketSnapshotAppender extends SnapshotAppender {
    private static final Logger LOG = Logger.getLogger(SmtpSnapshotAppender.class);
    private int port = 9289;
    private String server;

    public SocketSnapshotAppender(final TriggeringEventEvaluator evaluator) {
        super(evaluator);
    }

    public SocketSnapshotAppender() {
        super();
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public void setServer(final String mailServer) {
        if (mailServer == null) {
            throw new IllegalArgumentException("mail server not specified");
        }
        this.server = mailServer;
    }

    @Override
    protected void writeSnapshot(final String message, final String details) {
        try {
            if (server == null) {
                throw new IllegalStateException("mail server not specified");
            }

            final Socket s = new Socket(server, port);

            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "8859_1"));

            out.write(message + "\n");
            out.write(details + "\n");

            out.flush();

            s.close();
        } catch (final ConnectException e) {
            LOG.info("failed to connect to server " + server);
        } catch (final Exception e) {
            LOG.info("failed to send email with log", e);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
