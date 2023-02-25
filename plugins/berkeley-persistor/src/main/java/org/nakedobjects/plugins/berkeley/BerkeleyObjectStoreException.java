package org.nakedobjects.plugins.berkeley;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;

public class BerkeleyObjectStoreException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public BerkeleyObjectStoreException() {
        super();
    }

    public BerkeleyObjectStoreException(String messageFormat, Object... args) {
        super(messageFormat, args);
    }

    public BerkeleyObjectStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BerkeleyObjectStoreException(String message) {
        super(message);
    }

    public BerkeleyObjectStoreException(Throwable cause) {
        super(cause);
    }

    
}


// Copyright (c) Naked Objects Group Ltd.
