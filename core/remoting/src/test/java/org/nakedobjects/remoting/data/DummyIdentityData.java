package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.remoting.data.common.IdentityData;


public class DummyIdentityData extends DummyReferenceData implements IdentityData {
    private static final long serialVersionUID = 1L;

    public DummyIdentityData(final Oid oid, final String type, final Version version) {
        super(oid, type, version);
    }

    public DummyIdentityData() {
        this(null, null, null);
    }

}
// Copyright (c) Naked Objects Group Ltd.
