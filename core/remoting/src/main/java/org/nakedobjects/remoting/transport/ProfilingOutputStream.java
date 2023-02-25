package org.nakedobjects.remoting.transport;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.nakedobjects.remoting.protocol.encoding.EncodingMarshaller;


public class ProfilingOutputStream extends OutputStream {
	
    private static final Logger LOG = Logger.getLogger(ProfilingOutputStream.class);

    private final OutputStream wrapped;
    private int bytes = 0;
    private long end = 0;
    private long start = 0;

    public ProfilingOutputStream(final OutputStream wrapped) {
        this.wrapped = wrapped;
    }

    private void end() {
        end = System.currentTimeMillis();
    }

    public int getSize() {
        return bytes;
    }

    public float getTime() {
        return (end - start) / 1000.0f;
    }

    public void resetTimer() {
        bytes = 0;
        start = end = 0;
    }

    private void start() {
        if (start == 0) {
            start = System.currentTimeMillis();
        }
    }

    @Override
    public void write(final byte[] b) throws IOException {
        start();
        bytes += b.length;
        wrapped.write(b);
        end();
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        start();
        bytes += len;
        wrapped.write(b, off, len);
        end();
    }

    @Override
    public void write(final int b) throws IOException {
        start();
        bytes++;
        wrapped.write(b);
        end();
    }
    
    @Override
    public void close() throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(getSize() + " bytes sent in " + getTime() + " seconds");
        }
        resetTimer();
        super.close();
    }

}

// Copyright (c) Naked Objects Group Ltd.
