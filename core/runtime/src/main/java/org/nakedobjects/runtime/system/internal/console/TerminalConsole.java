package org.nakedobjects.runtime.system.internal.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class TerminalConsole implements ServerConsole, Runnable {
    private static final Logger LOG = Logger.getLogger(TerminalConsole.class);
    private Server server;
    private boolean running = true;

    public TerminalConsole() {
        new Thread(this).start();
    }

    public void clear() {}

    public void close() {
        running = false;
    }

    public void collections() {}

    public void init(final Server server) {
        this.server = server;
        log("Control of " + server);
    }

    public void listClasses() {
    /*
     * try { Enumeration e = server.getObjectStore().classes();
     * 
     * log("Loaded classes:-"); while (e.hasMoreElements()) { NakedClass object = (NakedClass)
     * e.nextElement();
     * 
     * log(" " + object); } } catch (ObjectStoreException e) { LOG.error("Error listing classes " +
     * e.getMessage()); }
     */
    }

    public void log() {
        log("");
    }

    public void log(final String message) {
        LOG.info(message);
        System.out.println("> " + message);
    }

    public void objects() {}

    public void quit() {
        server.shutdown();
        server = null;
        running = false;
    }

    public void run() {
        final BufferedReader dis = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (running) {
                final String s = dis.readLine().toLowerCase();

                if (s.equals("")) {
                    continue;
                } else if (s.equals("quit")) {
                    quit();
                } else if (s.equals("classes")) {
                    listClasses();
                } else {
                    System.out.println("Commands: classes, quit");
                }
            }
        } catch (final IOException e) {
            quit();
        }
        System.exit(0);
    }

    public void start() {
        new Thread(this).start();
    }
}

// Copyright (c) Naked Objects Group Ltd.

