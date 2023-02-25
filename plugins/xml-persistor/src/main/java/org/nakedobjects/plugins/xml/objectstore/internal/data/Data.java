package org.nakedobjects.plugins.xml.objectstore.internal.data;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public abstract class Data {
	private final NakedObjectSpecification noSpec;
    private final SerialOid oid;
    private final FileVersion version;

    Data(final NakedObjectSpecification noSpec, final SerialOid oid, final FileVersion version) {
    	this.noSpec = noSpec;
        this.oid = oid;
        this.version = version;
    }

    public SerialOid getOid() {
        return oid;
    }

    public FileVersion getVersion() {
        return version;
    }

    public NakedObjectSpecification getSpecification() {
		return noSpec;
	}
    
    public String getTypeName() {
        return noSpec.getFullName();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Data) {
            return ((Data) obj).getTypeName().equals(getTypeName()) && ((Data) obj).oid.equals(oid);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + getTypeName().hashCode();
        h = 37 * h + oid.hashCode();
        return h;
    }

}
// Copyright (c) Naked Objects Group Ltd.
