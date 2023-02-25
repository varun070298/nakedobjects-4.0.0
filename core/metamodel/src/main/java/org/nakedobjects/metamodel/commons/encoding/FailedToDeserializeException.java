/**
 * 
 */
package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;

public class FailedToDeserializeException extends IOException {

	private static final long serialVersionUID = 1L;
	
	private Throwable cause;
	
	public FailedToDeserializeException(ClassNotFoundException cause) {
		this.cause = cause;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
	
}