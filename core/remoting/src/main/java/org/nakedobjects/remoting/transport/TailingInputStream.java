package org.nakedobjects.remoting.transport;

import java.io.IOException;
import java.io.InputStream;


/**
 * Collection of timing and quantity data for the stream. Note that as the reads block the clock does not
 * start until the first read has completed.
 */
public class TailingInputStream extends InputStream {
    private final InputStream wrapped;

    public TailingInputStream(final InputStream wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int read() throws IOException {
        final int read = wrapped.read();
        System.out.println("byte " + read);
        return read;
    }

    @Override
    public int read(final byte[] b) throws IOException {
        final int read = wrapped.read(b);
        System.out.println("bytes (" + read + ")" + new String(b));
        return read;
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int read = wrapped.read(b, off, len);
        System.out.println("bytes (" + read + ")" + new String(b, off, len));
        return read;
    }

}

// Copyright (c) Naked Objects Group Ltd.
