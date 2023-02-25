package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.oid.stringable.directly.DirectlyStringableOid;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.ensure.Assert;


public final class SerialOid implements Oid, Encodable, Serializable, DirectlyStringableOid {

	private static final long serialVersionUID = 1L;
    
    static enum State {
        PERSISTENT,
        TRANSIENT;
        public boolean isTransient() {
            return this == TRANSIENT;
        }
    }

    public static SerialOid createPersistent(final long serialNo) {
        return new SerialOid(serialNo, State.PERSISTENT);
    }

    public static SerialOid createTransient(final long serialNo) {
        return new SerialOid(serialNo, State.TRANSIENT);
    }

    private int hashCode;
    private State state;
    private SerialOid previous;
    private long newSerialNo;
    private long serialNo;
    private String toString;
    private String enString;

    private static final String TOSTRING_SERIAL_NUM_PREFIX = "#";
    private static final String TOSTRING_PREVIOUS_CONCAT = "+";

    private static final String ENSTRING_SERIAL_NUM_PREFIX = ":";
    private static final String ENSTRING_PREVIOUS_CONCAT = "~";
	private static Pattern DESTRING_PATTERN = Pattern.compile("^(T?)OID" + ENSTRING_SERIAL_NUM_PREFIX + "(-?[0-9A-F]+)(" + ENSTRING_PREVIOUS_CONCAT + "(T?)OID" + ENSTRING_SERIAL_NUM_PREFIX + "(-?[0-9A-F]+))?$");

    //////////////////////////////////////////////
    // Constructor
	// encode, decode
    //////////////////////////////////////////////
    
    private SerialOid(final long serialNo, final State state) {
        this.serialNo = serialNo;
        this.state = state;
        initialized();
    }

    public SerialOid(final DataInputExtended input) throws IOException {
    	this.serialNo = input.readLong();
        this.state = input.readBoolean()?State.TRANSIENT:State.PERSISTENT;
        final boolean hasPrevious = input.readBoolean();
        
        if (hasPrevious) {
            final long previousSerialNo = input.readLong();
            final State previousState = input.readBoolean()?State.TRANSIENT:State.PERSISTENT;
			this.previous = new SerialOid(previousSerialNo, previousState);
        }
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeLong(this.serialNo);
		output.writeBoolean(this.state.isTransient());
        boolean hasPrevious = previous != null;
		output.writeBoolean(hasPrevious);
		
        if (hasPrevious) {
            output.writeLong(previous.serialNo);
			output.writeBoolean(previous.state.isTransient());
        }
    }

	private void initialized() {
		cacheState();
	}

    
    //////////////////////////////////////////////
    // Directly Stringable
    //////////////////////////////////////////////

    /**
     * @see DirectlyStringableOid
     * @see #enString()
     */
    public static SerialOid deString(String oidStr) {
    	Matcher matcher = DESTRING_PATTERN.matcher(oidStr);
    	if (!matcher.matches()) {
    		throw new IllegalArgumentException("Could not parse OID '" + oidStr + "'; should match pattern: " + DESTRING_PATTERN.toString());
    	}
    	
    	String transientStr = matcher.group(1);
    	String serialNumInHexStr = matcher.group(2);
    	SerialOid oid = createOid(transientStr, serialNumInHexStr);
    	
    	String previousStr = matcher.group(3);
    	if (!StringUtils.isEmpty(previousStr)) {
    		String previousTransientStr = matcher.group(4);
    		String previousSerialNumInHexStr = matcher.group(5);
    		
        	oid.previous = createOid(previousTransientStr, previousSerialNumInHexStr);
    	}
    	
    	return oid;
    }

	private static SerialOid createOid(String transientStr, String serialNumInHexStr) {
		boolean isTransient = "T".equals(transientStr);
		long serialNum = Long.parseLong(serialNumInHexStr, 16);
		return isTransient ? SerialOid.createTransient(serialNum) : SerialOid.createPersistent(serialNum);
	}
    

    /**
     * Returns a well-defined format which can be converted back using {@link #deString(String)}.
     * 
     * <p>
     * The options are:
     * <ul>
     * <li>For transient with no previous: <tt>TOID#12AB</tt> where the initial T indicates transient and after the 
     *     # is the serial number in hex.</li>
     * <li>For persistent with previous: <tt>OID#12ED+TOID#12AB</tt> where after the + is the previous OID, encoded</li>
     * <li>For persistent with no previous: <tt>OID#12ED</tt>.</li>
     * 
     * @see DirectlyStringableOid
     * @see #deString(String)
     */
	public String enString() {
		return enString;
	}

    //////////////////////////////////////////////
    // isTransient
    //////////////////////////////////////////////

    public boolean isTransient() {
        return state.isTransient();
    }


    //////////////////////////////////////////////
    // copyFrom
    //////////////////////////////////////////////

    public void copyFrom(final Oid oid) {
        Assert.assertTrue(oid instanceof SerialOid);
        final SerialOid from = (SerialOid) oid;
        this.serialNo = from.serialNo;
        this.state = from.state;
        cacheState();
    }


    //////////////////////////////////////////////
    // Previous
    //////////////////////////////////////////////

    public Oid getPrevious() {
        return previous;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public void clearPrevious() {
        previous = null;
    }


    //////////////////////////////////////////////
    // SerialNo (not API)
    //////////////////////////////////////////////

    /**
     * Should be called prior to {@link #makePersistent()}
     */
    public void setId(final long serialNo) {
        Assert.assertTrue(state.isTransient());
    	this.newSerialNo = serialNo;
    }

    public void makePersistent() {
        Assert.assertTrue(state.isTransient());
        Assert.assertTrue(newSerialNo != 0);
        previous = new SerialOid(this.serialNo, state);
        this.serialNo = newSerialNo;
        this.state = State.PERSISTENT;
        cacheState();
    }

    public long getSerialNo() {
        return serialNo;
    }

    
    //////////////////////////////////////////////
    // equals, hashCode
    //////////////////////////////////////////////

    private void cacheState() {
        hashCode = 17;
        hashCode = 37 * hashCode + (int) (serialNo ^ (serialNo >>> 32));
        hashCode = 37 * hashCode + (isTransient() ? 0 : 1);
		toString = asString(this, TOSTRING_SERIAL_NUM_PREFIX) + (previous == null ? "" : TOSTRING_PREVIOUS_CONCAT);
		//enString = asString(this, ENSTRING_SERIAL_NUM_PREFIX) + (previous == null ? "" : ENSTRING_PREVIOUS_CONCAT + asString(previous, ENSTRING_SERIAL_NUM_PREFIX));
		enString = asString(this, ENSTRING_SERIAL_NUM_PREFIX);
    }

	private String asString(SerialOid x, String serialNumPrefix) {
		return (x.isTransient() ? "T" : "") + "OID" + serialNumPrefix + Long.toString(x.serialNo, 16).toUpperCase();
	}


    /*
     * public void setPrevious(SerialOid previous) { Assert.assertNull(previous); this.previous = previous; }
     */

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        return equals((SerialOid)other);
    }

    /**
     * Overloaded to allow compiler to link directly if we know the compile-time type.
     * (possible performance improvement - called 166,000 times in normal ref data fixture.
     */
    public boolean equals(final SerialOid other) {
        return other.serialNo == serialNo && other.state == state;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return toString;
    }

}
// Copyright (c) Naked Objects Group Ltd.
