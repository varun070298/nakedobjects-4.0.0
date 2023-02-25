package org.nakedobjects.remoting.data.common;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class IdentityDataImpl implements IdentityData, Encodable {
	
    private static final long serialVersionUID = 1L;
    private final Oid oid;
    private final String type;
    private final Version version;

    public IdentityDataImpl(final String type, final Oid oid, final Version version) {
        this.type = type;
        this.oid = oid;
        this.version = version;
        initialized();
    }

    public IdentityDataImpl(final DataInputExtended input) throws IOException {
        this.type = input.readUTF();
        this.oid = input.readEncodable(Oid.class);
        this.version = input.readEncodable(Version.class);
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(type);
        output.writeEncodable(oid);
        output.writeEncodable(version);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    
    public Oid getOid() {
        return oid;
    }

    public String getType() {
        return type;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("oid", oid);
        str.append("version", version);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
