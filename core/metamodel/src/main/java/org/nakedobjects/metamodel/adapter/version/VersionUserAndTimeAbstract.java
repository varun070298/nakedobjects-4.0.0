package org.nakedobjects.metamodel.adapter.version;

import java.io.IOException;
import java.util.Date;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;



public abstract class VersionUserAndTimeAbstract extends VersionUserAbstract {
    private final Date time;

    public VersionUserAndTimeAbstract(final String user, final Date time) {
    	super(user);
        this.time = time;
        initialized();
    }

    public VersionUserAndTimeAbstract(DataInputExtended input) throws IOException {
    	super(input);
    	this.time = new Date(input.readLong());
    	initialized();
	}

    @Override
    public void encode(final DataOutputExtended output) throws IOException {
        super.encode(output);
        output.writeLong(time.getTime());
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////


	public Date getTime() {
        return time;
    }


}
// Copyright (c) Naked Objects Group Ltd.
