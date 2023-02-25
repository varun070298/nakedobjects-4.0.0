package org.nakedobjects.metamodel.commons.io;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.io.InputStream;

import org.nakedobjects.metamodel.commons.ensure.Ensure;

/**
 * An input stream that reads from an underlying {@link InputStream}, deferring
 * the interactions until needed.
 * 
 * <p>
 * This other stream is provided as needed by an {@link InputStreamProvider} so
 * that the underlying stream is not eagerly loaded.
 */
public class LazyInputStream extends InputStream {

	/**
	 * An interface to be implemented by clients that wish to utilize
	 * {@link LazyInputStream}s. The implementation of this interface should
	 * defer obtaining the desired input stream until absolutely necessary.
	 */
	public static interface InputStreamProvider {
		InputStream getInputStream() throws IOException;
	}

	private InputStreamProvider provider;

	private InputStream underlying = null;

	// ///////////////////////////////////////////////////////
	// Constructor
	// ///////////////////////////////////////////////////////

	/**
	 * Construct a new lazy stream based off the given provider.
	 * 
	 * @param provider
	 *            the input stream provider. Must not be <code>null</code>.
	 */
	public LazyInputStream(InputStreamProvider provider) {
		Ensure.ensureThatArg(provider, is(not(nullValue())));
		this.provider = provider;
	}

	// ///////////////////////////////////////////////////////
	// InputStream API
	// ///////////////////////////////////////////////////////

	@Override
	public void close() throws IOException {
		obtainUnderlyingIfRequired();
		underlying.close();
	}

	@Override
	public int available() throws IOException {
		obtainUnderlyingIfRequired();
		return underlying.available();
	}

	@Override
	public void mark(int readlimit) {
		try {
			obtainUnderlyingIfRequired();
			underlying.mark(readlimit);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean markSupported() {
		try {
			obtainUnderlyingIfRequired();
			return underlying.markSupported();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int read() throws IOException {
		obtainUnderlyingIfRequired();
		return underlying.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		obtainUnderlyingIfRequired();
		return underlying.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		obtainUnderlyingIfRequired();
		return underlying.read(b);
	}

	@Override
	public long skip(long n) throws IOException {
		obtainUnderlyingIfRequired();
		return underlying.skip(n);
	}

	@Override
	public void reset() throws IOException {
		obtainUnderlyingIfRequired();
		underlying.reset();
	}

	// ///////////////////////////////////////////////////////
	// helpers
	// ///////////////////////////////////////////////////////

	private void obtainUnderlyingIfRequired() throws IOException {
		if (underlying == null) {
			underlying = provider.getInputStream();
		}
	}

	// ///////////////////////////////////////////////////////
	// equals, hashCode
	// ///////////////////////////////////////////////////////

	@Override
	public boolean equals(Object obj) {
		try {
			obtainUnderlyingIfRequired();
			return underlying.equals(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int hashCode() {
		try {
			obtainUnderlyingIfRequired();
			return underlying.hashCode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// ///////////////////////////////////////////////////////
	// toString
	// ///////////////////////////////////////////////////////

	public String toString() {
		try {
			obtainUnderlyingIfRequired();
			return underlying.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
