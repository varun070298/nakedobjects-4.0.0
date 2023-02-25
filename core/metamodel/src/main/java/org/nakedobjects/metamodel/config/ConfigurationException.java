package org.nakedobjects.metamodel.config;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ConfigurationException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(final String s) {
        super(s);
    }

    public ConfigurationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public ConfigurationException(final Throwable cause) {
        super(cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
