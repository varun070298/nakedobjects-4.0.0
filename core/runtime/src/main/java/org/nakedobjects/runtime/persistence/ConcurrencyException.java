package org.nakedobjects.runtime.persistence;

import java.text.DateFormat;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class ConcurrencyException extends ObjectPersistenceException {
    private final static DateFormat DATE_TIME = DateFormat.getDateTimeInstance();
    private static final long serialVersionUID = 1L;
    private Oid source;

    public ConcurrencyException(final NakedObject object, final Version updated) {
        this(object.getVersion().getUser() + " changed " + object.titleString() + " at "
                + DATE_TIME.format(object.getVersion().getTime()) + "\n\n" + object.getVersion() + " ~ " + updated + "", object
                .getOid());
    }

    public ConcurrencyException(final String message, final Oid source) {
        super(message);
        this.source = source;
    }

    public ConcurrencyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public Oid getSource() {
        return source;
    }
}
// Copyright (c) Naked Objects Group Ltd.
