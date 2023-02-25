package org.nakedobjects.plugins.hibernate.objectstore.metamodel.version;

import java.io.IOException;
import java.util.Date;

import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.adapter.version.VersionUserAbstract;
import org.nakedobjects.metamodel.adapter.version.VersionUserAndTimeAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;


public class LongVersion implements Version, Encodable {
	
    private static final long serialVersionUID = 1L;
    
    private String user;
    private Date time;
    private Long versionNumber;

    public LongVersion() {
    	this.time = null;
    	initialized();
	}

    public LongVersion(final Long versionNumber, final String user, final Date time) {
    	this.user = user;
    	this.time = time;
        this.versionNumber = versionNumber;
        initialized();
    }

    public LongVersion(DataInputExtended input) throws IOException {
    	user = input.readUTF();
    	boolean hasTime = input.readBoolean();
    	if (hasTime) {
    		this.time = new Date(input.readLong());
    	}
        this.versionNumber = input.readLong();
        initialized();
    }

    public void encode(DataOutputExtended output)
    		throws IOException {
		output.writeUTF(user);
    	boolean hasTime = time == null;
    	output.writeBoolean(hasTime);
    	if (hasTime) {
    		output.writeLong(time.getTime());
    	}
    	output.writeLong(versionNumber);
    }
    
	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////

	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Date getTime() {
		return getTime();
	}

	public void setTime(final Date time) {
		this.time = time;
	}
	
    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(final Long versionNumber) {
        this.versionNumber = versionNumber;
    }


    /////////////////////////////////////////////////////////
    // different
    /////////////////////////////////////////////////////////

    public boolean different(final Version version) {
        if (!(version instanceof LongVersion)) {
            return false;
        } 
        final LongVersion other = (LongVersion) version;
		return !versionNumber.equals(other.versionNumber);
    }


    /////////////////////////////////////////////////////////
    // equals, hashCode
    /////////////////////////////////////////////////////////

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof LongVersion) {
            return !different((LongVersion) obj);
        } else {
            return false;
        }
    }


    /**
     * TODO: dubious - if {@link #setVersionNumber(Long) version number set} then will invalidate.
     */
    @Override
    public int hashCode() {
        return (int) (versionNumber.longValue() ^ (versionNumber.longValue() >>> 32));
    }


    /////////////////////////////////////////////////////////
    // toString, sequence
    /////////////////////////////////////////////////////////

    public String sequence() {
        return Long.toString(versionNumber, 16);
    }


    @Override
    public String toString() {
        return "LongVersion#" + versionNumber + " " + ToString.timestamp(getTime());
    }

}
// Copyright (c) Naked Objects Group Ltd.
