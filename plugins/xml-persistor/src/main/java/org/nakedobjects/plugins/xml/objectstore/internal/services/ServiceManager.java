package org.nakedobjects.plugins.xml.objectstore.internal.services;

import org.nakedobjects.metamodel.adapter.oid.Oid;


public interface ServiceManager {

    void loadServices();

    void registerService(String name, Oid oid);

    Oid getOidForService(String name);

}

// Copyright (c) Naked Objects Group Ltd.
