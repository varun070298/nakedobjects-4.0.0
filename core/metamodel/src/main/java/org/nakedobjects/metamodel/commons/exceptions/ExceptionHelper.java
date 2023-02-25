package org.nakedobjects.metamodel.commons.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class ExceptionHelper {

    public static String exceptionTraceAsString(final Throwable exception) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            exception.printStackTrace(new PrintStream(baos));
            return baos.toString();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (final IOException ignore) {}
            }
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
