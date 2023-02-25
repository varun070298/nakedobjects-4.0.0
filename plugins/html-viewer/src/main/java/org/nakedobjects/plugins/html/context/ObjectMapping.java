package org.nakedobjects.plugins.html.context;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.debug.DebugString;


public interface ObjectMapping {

    Oid getOid();

    NakedObject getObject();

    Version getVersion();

    void checkVersion(NakedObject object);

    void restoreToLoader();

    void debug(DebugString debug);

    void updateVersion();
}

// Copyright (c) Naked Objects Group Ltd.
