package org.nakedobjects.plugins.xml.objectstore.internal.services;

import org.nakedobjects.metamodel.adapter.oid.Oid;


public class DummyServiceManager implements ServiceManager {

    public Oid getOidForService(final String name) {
        return null;
    }

    public void loadServices() {}

    public void registerService(final String name, final Oid oid) {}

}

// Copyright (c) Naked Objects Group Ltd.
