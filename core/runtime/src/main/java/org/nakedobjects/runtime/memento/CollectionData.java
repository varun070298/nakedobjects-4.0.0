package org.nakedobjects.runtime.memento;

import java.io.IOException;

import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;


class CollectionData extends Data {
	
    private final static long serialVersionUID = 1L;
    final Data[] elements;

    public CollectionData(final Oid oid, ResolveState resolveState, final String className, final Data[] elements) {
        super(oid, resolveState.name(), className);
        this.elements = elements;
        initialized();
    }

    public CollectionData(final DataInputExtended input) throws IOException {
        super(input);
        this.elements = input.readEncodables(Data.class);
        initialized();
    }

    @Override
    public void encode(final DataOutputExtended output) throws IOException {
        super.encode(output);
        output.writeEncodables(elements);
    }

	public void initialized() {
		// nothing to do
	}

    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        for (int i = 0; i < elements.length; i++) {
            debug.appendln("" + i + 1, elements[i]);

            // TODO recurse!
        }
    }

    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer("(");
        for (int i = 0; i < elements.length; i++) {
            str.append((i > 0) ? "," : "");
            str.append(elements[i]);
        }
        str.append(")");
        return str.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
