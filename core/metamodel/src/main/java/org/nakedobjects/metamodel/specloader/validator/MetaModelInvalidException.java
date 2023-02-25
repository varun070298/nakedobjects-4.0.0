package org.nakedobjects.metamodel.specloader.validator;

public class MetaModelInvalidException extends IllegalStateException {

	private static final long serialVersionUID = 1L;

	public MetaModelInvalidException() {
	}

	public MetaModelInvalidException(String s) {
		super(s);
	}

	public MetaModelInvalidException(Throwable cause) {
		super(cause);
	}

	public MetaModelInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}
