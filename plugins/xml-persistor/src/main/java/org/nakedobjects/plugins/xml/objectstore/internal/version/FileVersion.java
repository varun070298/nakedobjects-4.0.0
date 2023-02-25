package org.nakedobjects.plugins.xml.objectstore.internal.version;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.adapter.version.VersionUserAndTimeAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.xml.objectstore.internal.clock.Clock;


public class FileVersion extends VersionUserAndTimeAbstract implements Encodable, Serializable {
    private static final long serialVersionUID = 1L;
    private static Clock clock;

    public static void setClock(final Clock clock) {
        FileVersion.clock = clock;
    }

    public FileVersion(final String user) {
        this(user, clock.getTime());
    }

    public FileVersion(final String user, final long sequence) {
        super(user, new Date(sequence));
        initialized();
    }

    public FileVersion(final DataInputExtended input) throws IOException {
        super(input);
        initialized();
    }

    @Override
    public void encode(final DataOutputExtended output) throws IOException {
    	super.encode(output);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    

    public long getSequence() {
        return getTime().getTime();
    }

    public String sequence() {
        return Long.toString(getSequence(), 16);
    }

    public boolean different(final Version version) {
        if (version instanceof FileVersion) {
            final FileVersion other = (FileVersion) version;
            return !sameTime(other);
        } else {
            return false;
        }
    }

    private boolean sameTime(final FileVersion other) {
        return getTime().getTime() == other.getTime().getTime();
    }

    // don't think is used...
//    @Override
//    protected VersionAbstract next() {
//        // return new FileVersion(user);
//        throw new NotYetImplementedException();
//    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FileVersion) {
            return sameTime((FileVersion) obj);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("sequence", getTime().getTime());
        str.append("time", getTime());
        str.append("user", getUser());
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
