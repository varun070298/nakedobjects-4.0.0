package org.nakedobjects.metamodel.adapter.oid;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;


/**
 * Used as the {@link Oid} for collections, values and <tt>@Aggregated</tt> types.
 * 
 * <p>
 * The Hibernate Object Store has custom handling for collections.
 */
public class AggregatedOid implements Oid, Serializable {

    private static final long serialVersionUID = 1L;

	private static <T> T ensureNotNull(final T oid, String message) {
		Assert.assertNotNull(message, oid);
		return oid;
	}

    private final Oid parentOid;
    private final String fieldName;
    
    private AggregatedOid previous;

	private int cachedHashCode;

	
    ///////////////////////////////////////////////////////////
    // Constructor, Encodeable
    ///////////////////////////////////////////////////////////

    public AggregatedOid(final Oid oid, final String id) {
        Assert.assertNotNull("Field required", id);
        this.parentOid = oid;
        this.fieldName = id;
        initialized();
    }

    public AggregatedOid(final Oid oid, final Identifier identifier) {
        this(oid,  
        	ensureNotNull(identifier, "Field required").getMemberName());
    }

    public AggregatedOid(DataInputExtended input) throws IOException {
    	this.parentOid = input.readEncodable(Oid.class);
    	this.fieldName = input.readUTF();
    	initialized();
    }
    
    public void encode(DataOutputExtended output) throws IOException {
    	output.writeEncodable(parentOid);
        output.writeUTF(fieldName);
    }

	private void initialized() {
		cacheState();
	}

    ///////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////

    public Oid getParentOid() {
        return parentOid;
    }

    public String getFieldName() {
        return fieldName;
    }

    
    ///////////////////////////////////////////////////////////
    // makePersistent
    ///////////////////////////////////////////////////////////

    public void makePersistent() {
        this.previous = new AggregatedOid(this.parentOid, this.fieldName);
        cacheState();
    }
    
    ///////////////////////////////////////////////////////////
    // Previous
    ///////////////////////////////////////////////////////////

    public Oid getPrevious() {
        return previous;
    }

    public boolean hasPrevious() {
        return false;
    }

    public void clearPrevious() {}


    
    ///////////////////////////////////////////////////////////
    // Other OID stuff
    ///////////////////////////////////////////////////////////

    public void copyFrom(final Oid oid) {
        throw new NotYetImplementedException();
    }

    public boolean isTransient() {
        return parentOid.isTransient();
    }

    
    ///////////////////////////////////////////////////////////
    // Value semantics
    ///////////////////////////////////////////////////////////
    
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        return equals((AggregatedOid)other);
    }
    
    public boolean equals(final AggregatedOid other) {
        return other.parentOid.equals(parentOid) && 
               other.fieldName.equals(fieldName);
    }

    @Override
    public int hashCode() {
        cacheState();
        return cachedHashCode;
    }

	private void cacheState() {
		int hashCode = 17;
        hashCode = 37 * hashCode + parentOid.hashCode();
        hashCode = 37 * hashCode + fieldName.hashCode();
        cachedHashCode = hashCode;
	}

    
    ///////////////////////////////////////////////////////////
    // toString
    ///////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "AOID[" + parentOid + "," + fieldName + "]";
    }



}

// Copyright (c) Naked Objects Group Ltd.
