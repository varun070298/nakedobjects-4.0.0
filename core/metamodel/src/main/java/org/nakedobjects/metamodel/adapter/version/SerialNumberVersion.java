package org.nakedobjects.metamodel.adapter.version;

import java.io.IOException;
import java.util.Date;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;


public class SerialNumberVersion extends VersionUserAndTimeAbstract {
	
    private final long versionNumber;

    public SerialNumberVersion(final long number, final String user, final Date time) {
        super(user, time);
        this.versionNumber = number;
        initialized();
    }

    public SerialNumberVersion(final DataInputExtended input) throws IOException {
        super(input);
        this.versionNumber = input.readLong();
        initialized();
    }

    @Override
    public void encode(final DataOutputExtended output) throws IOException {
    	super.encode(output);
        output.writeLong(versionNumber);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    public long getSequence() {
        return versionNumber;
    }

    public String sequence() {
        return Long.toString(versionNumber, 16);
    }

    // don't think is used
//    @Override
//    protected VersionAbstract next() {
//        return new SerialNumberVersion(versionNumber + 1, null, null);
//    }

    public boolean different(final Version version) {
        if (version instanceof SerialNumberVersion) {
            final SerialNumberVersion other = (SerialNumberVersion) version;
            return versionNumber != other.versionNumber;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof SerialNumberVersion) {
            return !different((SerialNumberVersion) obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) (versionNumber ^ (versionNumber >>> 32));
    }

    @Override
    public String toString() {
        return "SerialNumberVersion#" + versionNumber + " " + ToString.timestamp(getTime());
    }


}
// Copyright (c) Naked Objects Group Ltd.
