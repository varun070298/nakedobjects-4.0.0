package org.nakedobjects.runtime.persistence.adaptermanager.internal;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.component.Resettable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;


public interface PojoAdapterMap extends DebugInfo, Iterable<NakedObject>, SessionScopedComponent, Resettable {

    void add(Object pojo, NakedObject adapter);

    boolean containsPojo(Object pojo);

    NakedObject getAdapter(Object pojo);

    void remove(NakedObject adapter);

}
// Copyright (c) Naked Objects Group Ltd.
