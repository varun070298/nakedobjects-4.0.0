package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;


public class TestProxyOid implements Oid {
    
    private static final long serialVersionUID = 1L;

    /**
     * pending, prior to {@link #makePersistent()}.
     */
    private int newId;
    int id;
    private TestProxyOid previous;
    public boolean isTransient = true;

    private int hashCode;


    /**
     * Creates transient.
     */
    public TestProxyOid(final int id) {
        this(id, false);
    }

    /**
     * Creates either persistent or transient.
     */
    public TestProxyOid(final int id, final boolean persistent) {
        this.id = id;
        this.isTransient = !persistent;
        cacheHashCode();
    }

    public void encode(DataOutputExtended outputStream) {
        throw new UnsupportedOperationException();
    }


    public boolean hasPrevious() {
        return previous != null;
    }

    public Oid getPrevious() {
        return previous;
    }

    public void copyFrom(final Oid oid) {
        this.id = ((TestProxyOid) oid).id;
        this.isTransient = ((TestProxyOid) oid).isTransient;
        cacheHashCode();
    }

    public boolean isTransient() {
        return isTransient;
    }

    /**
     * Should be called prior to makePersistent
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    public void makePersistent() {
        this.previous = new TestProxyOid(this.id, !this.isTransient);
        this.isTransient = false;
        this.id = newId;
    }

    public void setupPrevious(final TestProxyOid previous) {
        this.previous = previous;
    }

    public void clearPrevious() {
        previous = null;
    }

	public void setNewId(int newId) {
		this.newId = newId;
		
	}

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TestProxyOid) {
            return ((TestProxyOid) obj).id == id && ((TestProxyOid) obj).isTransient == isTransient;
        }
        return false;
    }

    private void cacheHashCode() {
        hashCode = 37 * 17 + (id ^ (id >>> 32)) + (isTransient ? 0 : 1);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "Oid#" + id + (isTransient ? " T" : "") + (hasPrevious() ? " (" + previous + ")" : "; hashCode=" + hashCode);
    }


}
// Copyright (c) Naked Objects Group Ltd.
