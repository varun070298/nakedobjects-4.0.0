package org.nakedobjects.plugins.html.crumb;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.html.request.Request;



public class ObjectFieldCrumb implements Crumb {
    private final String fieldName;

    public ObjectFieldCrumb(final String fieldName) {
        this.fieldName = fieldName;
    }

    public void debug(final DebugString string) {
        string.appendln("Object Field Crumb");
        string.appendln("field name", fieldName);
    }

    public String title() {
        return fieldName;
    }

    @Override
    public String toString() {
        return new ToString(this).append(title()).toString();
    }

    public Request changeContext() {
        throw new NotYetImplementedException();
    }
}

// Copyright (c) Naked Objects Group Ltd.
