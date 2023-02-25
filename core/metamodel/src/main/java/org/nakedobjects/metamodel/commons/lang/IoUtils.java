package org.nakedobjects.metamodel.commons.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class IoUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private IoUtils() {}

    /**
     * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
     * 
     * @param input
     *            the <code>InputStream</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws IllegalArgumentException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        if (output == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

	public static void closeSafely(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public static void closeSafely(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException ignore) {}
        }
	}

}

// Copyright (c) Naked Objects Group Ltd.
