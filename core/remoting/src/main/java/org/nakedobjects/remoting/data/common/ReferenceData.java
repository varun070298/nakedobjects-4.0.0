package org.nakedobjects.remoting.data.common;

import java.io.Serializable;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.remoting.data.Data;


public interface ReferenceData extends Data, Serializable {
    Oid getOid();

    Version getVersion();
}
// Copyright (c) Naked Objects Group Ltd.
