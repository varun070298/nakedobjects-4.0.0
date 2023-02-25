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
import org.nakedobjects.remoting.data.Data;

public class ObjectDataImpl implements ObjectData, Encodable {
	
    private static final long serialVersionUID = 1L;
    private final Oid oid;
    private final boolean isResolved;
    private final String type;
    private final Version version;
    private Data fieldContent[];

    public ObjectDataImpl(
    		final Oid oid, final String type, final boolean resolved, final Version version) {
        this.type = type;
        this.oid = oid;
        this.version = version;
        this.isResolved = resolved;
        // fieldContent initially null
        initialized();
    }

    public ObjectDataImpl(final DataInputExtended input) throws IOException {
        this.type = input.readUTF();
        this.oid = input.readEncodable(Oid.class);
        this.version = input.readEncodable(Version.class);
        this.isResolved = input.readBoolean();
        this.fieldContent = input.readEncodables(Data.class);
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(type);
        output.writeEncodable(oid);
        output.writeEncodable(version);
        output.writeBoolean(isResolved);
        output.writeEncodables(fieldContent);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    

    public Data[] getFieldContent() {
        return fieldContent;
    }

    public Oid getOid() {
        return oid;
    }

    public String getType() {
        return type;
    }

    public Version getVersion() {
        return version;
    }

    public boolean hasCompleteData() {
        return isResolved;
    }

    public void setFieldContent(final Data[] fieldContent) {
        this.fieldContent = fieldContent;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("oid", oid);
        str.append("version", version);
        str.append(",fields=");
        for (int i = 0; fieldContent != null && i < fieldContent.length; i++) {
            if (i > 0) {
                str.append(";");
            }
            if (fieldContent[i] == null) {
                str.append("null");
            } else {
                final String name = fieldContent[i].getClass().getName();
                str.append(name.substring(name.lastIndexOf('.') + 1));
            }
        }
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
