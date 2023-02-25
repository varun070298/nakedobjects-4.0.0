package org.nakedobjects.metamodel.commons.logging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class SnapshotServer {
    private static final String SNAPSHOT_PROPERTIES = "snapshot.properties";
    private static final Logger LOG = Logger.getLogger(SnapshotServer.class);

    public static void main(final String[] args) {
        BasicConfigurator.configure();

        int port;
        String directoryPath;
        String fileName;
        String extension;

        final Properties prop = new Properties();
        FileInputStream propIn = null;
        try {
            propIn = new FileInputStream(SNAPSHOT_PROPERTIES);
            prop.load(propIn);
        } catch (final FileNotFoundException e) {
            LOG.error("failed to load properties file, " + SNAPSHOT_PROPERTIES);
            return;
        } catch (final IOException e) {
            LOG.error("failed to read properties file, " + SNAPSHOT_PROPERTIES, e);
            return;
        } finally {
            if (propIn != null) {
                try {
                    propIn.close();
                } catch (final IOException e) {
                    LOG.error("failed to close properties file, " + SNAPSHOT_PROPERTIES, e);
                    return;
                }
            }
        }

        final String prefix = "nakedobjects.snapshot.";
        final String portString = prop.getProperty(prefix + "port", "9289");
        port = Integer.valueOf(portString).intValue();
        directoryPath = prop.getProperty(prefix + "directory", ".");
        fileName = prop.getProperty(prefix + "filename", "log-snapshot-");
        extension = prop.getProperty(prefix + "extension", "txt");

        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (final IOException e) {
            LOG.error("failed to start server", e);
            return;
        }

        while (true) {
            try {
                final Socket s = server.accept();

                LOG.info("receiving log from " + s.getInetAddress().getHostName());

                final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "8859_1"));

                final String message = in.readLine();
                final SnapshotWriter w = new SnapshotWriter(directoryPath, fileName, extension, message);
                String line;
                while ((line = in.readLine()) != null) {
                    w.appendLog(line);
                }
                s.close();

                in.close();
            } catch (final IOException e) {
                LOG.error("failed to log", e);
            }
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
