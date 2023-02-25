package org.nakedobjects.plugins.html.crumb;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;



public class ObjectCrumb implements Crumb {
    private final String objectId;
    private final boolean isService;
    private final String title;

    public ObjectCrumb(final String objectId, final NakedObject object) {
        this.objectId = objectId;
        title = object.titleString();
        isService = object.getSpecification().isService();
    }

    public void debug(final DebugString string) {
        string.appendln("Object Crumb");
        string.appendln("object", objectId);
        string.appendln("title", title);
        string.appendln("for service", isService);
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return new ToString(this).append(title()).toString();
    }

    public Request changeContext() {
        if (isService) {
            return ForwardRequest.viewService(objectId);
        } else {
            return ForwardRequest.viewObject(objectId);
        }

    }
}

// Copyright (c) Naked Objects Group Ltd.
