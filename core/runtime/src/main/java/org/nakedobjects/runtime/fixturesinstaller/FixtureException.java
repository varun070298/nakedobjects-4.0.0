package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class FixtureException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public FixtureException() {
        super();
    }

    public FixtureException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public FixtureException(final String msg) {
        super(msg);
    }

    public FixtureException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
