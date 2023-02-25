package org.nakedobjects.runtime.system.internal.console;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;


public class FileConsole implements ServerConsole {
    final static Logger LOG = Logger.getLogger(FileConsole.class);
    private DataOutputStream dos;

    public void close() {}

    public void init(final Server server) {}

    public void log() {
        log("");
    }

    public void log(final String message) {
        try {
            LOG.info(message);
            dos = new DataOutputStream(new FileOutputStream("log.xxx"));
            dos.writeBytes(new Date() + " " + message + '\n');
            dos.close();
        } catch (final IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
