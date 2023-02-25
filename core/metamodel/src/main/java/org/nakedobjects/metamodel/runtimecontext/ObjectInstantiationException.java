package org.nakedobjects.metamodel.runtimecontext;

public class ObjectInstantiationException extends Exception {

    private static final long serialVersionUID = 1L;

    public ObjectInstantiationException() {
    }

    public ObjectInstantiationException(String message) {
        super(message);
    }

    public ObjectInstantiationException(Throwable cause) {
        super(cause);
    }

    public ObjectInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

}
