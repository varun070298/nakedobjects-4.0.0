package org.nakedobjects.plugins.html.crumb;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;



public class CollectionCrumb implements Crumb {
    private final String collectionId;
    private final String title;

    public CollectionCrumb(final String collectionId, final NakedObject collection) {
        this.collectionId = collectionId;
        title = collection.titleString();
    }

    public void debug(final DebugString string) {
        string.appendln("Collection Crumb");
        string.appendln("object", collectionId);
        string.appendln("title", title);
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return new ToString(this).append(title()).toString();
    }

    public Request changeContext() {
        return ForwardRequest.listCollection(collectionId);
    }
}

// Copyright (c) Naked Objects Group Ltd.
