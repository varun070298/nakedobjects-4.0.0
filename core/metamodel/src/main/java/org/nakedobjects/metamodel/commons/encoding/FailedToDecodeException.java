/**
 * 
 */
package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;

public class FailedToDecodeException extends IOException {

	private static final long serialVersionUID = 1L;
	
	private Throwable cause;
	
	public FailedToDecodeException(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
	
}