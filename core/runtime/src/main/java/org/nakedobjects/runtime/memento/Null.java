package org.nakedobjects.runtime.memento;

import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;


class Null implements Encodable, Serializable {
    private static final long serialVersionUID = 1L;

    public Null() {
    	initialized();
    }

    public Null(final DataInputExtended input) {
    	initialized();
    }

    public void encode(final DataOutputExtended output) {
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    

    @Override
    public String toString() {
        return "NULL";
    }

}
// Copyright (c) Naked Objects Group Ltd.
