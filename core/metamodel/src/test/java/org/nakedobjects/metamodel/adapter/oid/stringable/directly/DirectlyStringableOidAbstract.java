package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;

public abstract class DirectlyStringableOidAbstract implements DirectlyStringableOid {

	public void clearPrevious() {
	}

	public void copyFrom(Oid oid) {
	}

	public Oid getPrevious() {
		return null;
	}

	public boolean hasPrevious() {
		return false;
	}

	public boolean isTransient() {
		return false;
	}

	public void makePersistent() {
	}

	public void encode(DataOutputExtended outputStream) {
	}

}
