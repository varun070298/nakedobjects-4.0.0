package org.nakedobjects.runtime.memento;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

class Data implements Encodable, Serializable {
	
	private final static long serialVersionUID = 1L;
	
	private final String className;
	private final String resolveState;
	private final Oid oid;

	public Data(final Oid oid, final String resolveState, final String className) {
		this.className = className;
		this.resolveState = resolveState;
		this.oid = oid;
		initialized();
	}

	public Data(final DataInputExtended input) throws IOException {
		this.className = input.readUTF();
		this.resolveState = input.readUTF(); // TODO: make ResolveState encodable?
		this.oid = input.readEncodable(Oid.class);
		initialized();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeUTF(className);
		output.writeUTF(resolveState);
		output.writeEncodable(oid);
	}

	private void initialized() {
		// nothing to do
	}


    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

	
	/**
	 * Note: could be <tt>null</tt> if represents a
	 * {@link ResolveState#isValue() standalone} adapter.
	 */
	public Oid getOid() {
		return oid;
	}

	public String getClassName() {
		return className;
	}

	public String getResolveState() {
		return resolveState;
	}

	public void debug(final DebugString debug) {
		debug.appendln(className);
		debug.appendln(oid != null ? oid.toString() : "null");
		debug.appendln(resolveState);
	}
	
	@Override
	public String toString() {
		return className + "/" + oid;
	}

}
// Copyright (c) Naked Objects Group Ltd.
