package org.nakedobjects.remoting.data.common;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ArrayUtils;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class CollectionDataImpl implements CollectionData, Encodable, Serializable {
    private static final long serialVersionUID = 1L;
    private final ReferenceData elements[];
    private final Oid oid;
    private final String collectionType;
    private final Version version;
    private final boolean hasAllElements;
    private final String elementType;

    public CollectionDataImpl(
            final Oid oid,
            final String collectionType,
            final String elementType,
            final ReferenceData[] elements,
            final boolean hasAllElements,
            final Version version) {
        this.collectionType = collectionType;
        this.elementType = elementType;
        this.oid = oid;
        this.version = version;
        this.hasAllElements = hasAllElements;
        this.elements = elements;
        initialized();
    }

    public CollectionDataImpl(final DataInputExtended input) throws IOException {
        this.collectionType = input.readUTF();
        this.elementType = input.readUTF();
        this.oid = input.readEncodable(Oid.class);
        this.version = input.readEncodable(Version.class);
        this.hasAllElements = input.readBoolean();
        this.elements = input.readEncodables(ReferenceData.class);
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(collectionType);
        output.writeUTF(elementType);
        output.writeEncodable(oid);
        output.writeEncodable(version);
        output.writeBoolean(hasAllElements);
        output.writeEncodables(elements);
    }

    private void initialized() {
    	// nothing to do
    }
    
    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    public ReferenceData[] getElements() {
        return elements;
    }

    public String getElementype() {
        return elementType;
    }

    public Oid getOid() {
        return oid;
    }

    public String getType() {
        return collectionType;
    }

    public Version getVersion() {
        return version;
    }

    public boolean hasAllElements() {
        return hasAllElements;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("collection type", collectionType);
        str.append("element type", elementType);
        str.append("oid", oid);
        str.append("version", version);
        str.append(",elements=");
        for (int i = 0; elements != null && i < elements.length; i++) {
            if (i > 0) {
                str.append(";");
            }
            if (elements[i] == null) {
                str.append("null");
            } else {
                final String name = elements[i].getClass().getName();
                str.append(name.substring(name.lastIndexOf('.') + 1));
            }
        }
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
