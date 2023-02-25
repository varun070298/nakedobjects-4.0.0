package org.nakedobjects.metamodel.specloader.internal.cache;

import java.util.Enumeration;
import java.util.Hashtable;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class SimpleSpecificationCache implements SpecificationCache {
    private final Hashtable specs = new Hashtable();

    public NakedObjectSpecification get(final String className) {
        return (NakedObjectSpecification) specs.get(className);
    }

    public void cache(final String className, final NakedObjectSpecification spec) {
        specs.put(className, spec);
    }

    public void clear() {
        specs.clear();
    }

    public NakedObjectSpecification[] allSpecifications() {
        final int size = specs.size();
        final NakedObjectSpecification[] cls = new NakedObjectSpecification[size];
        final Enumeration e = specs.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            cls[i++] = (NakedObjectSpecification) e.nextElement();
        }
        return cls;
    }

}
// Copyright (c) Naked Objects Group Ltd.
