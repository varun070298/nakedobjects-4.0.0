package org.nakedobjects.metamodel.commons.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;


public class NakedObjectException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static boolean THROWABLE_SUPPORTS_CAUSE;

    static {
        // Java 1.4, and after, holds a cause; Java 1.1, 1.2 and .Net do not, so we need to
        // do the work ourselves.
        final Class<?> c = Throwable.class;
        try {
            THROWABLE_SUPPORTS_CAUSE = c.getMethod("getCause", new Class[0]) != null;
        } catch (final Exception ignore) {
            // this should never occur in proper Java environment
            THROWABLE_SUPPORTS_CAUSE = false;
        }
    }

    private final Throwable cause;

    public NakedObjectException() {
        super("");
        cause = null;
    }

    public NakedObjectException(final String message) {
        super(message);
        cause = null;
    }

    public NakedObjectException(final String messageFormat, Object... args) {
        super(MessageFormat.format(messageFormat, args));
        cause = null;
    }

    public NakedObjectException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public NakedObjectException(final Throwable cause) {
        super(cause == null ? null : cause.toString());
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return (cause == this ? null : cause);
    }

    @Override
    public void printStackTrace(final PrintStream s) {
        if (THROWABLE_SUPPORTS_CAUSE) {
            super.printStackTrace(s);
        } else {
            synchronized (s) {
                super.printStackTrace(s);
                final Throwable c = getCause();
                if (c != null) {
                    s.print("Root cause: ");
                    c.printStackTrace(s);
                }
            }
        }
    }

    @Override
    public void printStackTrace(final PrintWriter s) {
        if (THROWABLE_SUPPORTS_CAUSE) {
            super.printStackTrace(s);
        } else {
            synchronized (s) {
                super.printStackTrace(s);
                final Throwable c = getCause();
                if (c != null) {
                    s.println("Root cause: ");
                    c.printStackTrace(s);
                }
            }
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
