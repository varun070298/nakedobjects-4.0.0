package org.nakedobjects.plugins.html.crumb;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.html.request.Request;



public interface Crumb {
    // TODO add icons to crumbs

    String title();

    void debug(DebugString string);

    Request changeContext();
}

// Copyright (c) Naked Objects Group Ltd.
