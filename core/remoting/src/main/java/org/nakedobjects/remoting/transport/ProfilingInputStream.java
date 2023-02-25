package org.nakedobjects.remoting.transport;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;


/**
 * Collection of timing and quantity data for the stream. Note that as the reads block the clock does not
 * start until the first read has completed.
 */
public class ProfilingInputStream extends InputStream {
	
    private static final Logger LOG = Logger.getLogger(ProfilingOutputStream.class);

    private final InputStream wrapped;
    private int bytes = 0;
    private long end = 0;
    private long start = 0;

    public ProfilingInputStream(final InputStream wrapped) {
        this.wrapped = wrapped;
    }

    private void end() {
        if (start == 0) {
            start = System.currentTimeMillis();
        }
        end = System.currentTimeMillis();
    }

    public int getSize() {
        return bytes;
    }

    public float getTime() {
        return (end - start) / 1000.0f;
    }

    @Override
    public int read() throws IOException {
        start();
        final int read = wrapped.read();
        bytes++;
        end();
        return read;
    }

    @Override
    public int read(final byte[] b) throws IOException {
        start();
        final int read = wrapped.read(b);
        bytes += read;
        end();
        return read;
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        start();
        final int read = wrapped.read(b, off, len);
        bytes += read;
        end();
        return read;
    }

    public void resetTimer() {
        bytes = 0;
        start = end = 0;
    }

    private void start() {}
    
    @Override
    public void close() throws IOException {
    	super.close();
        if (LOG.isDebugEnabled()) {
            LOG.debug(getSize() + " bytes received in " + getTime() + " seconds");
        }
        resetTimer();
    }
}

// Copyright (c) Naked Objects Group Ltd.
