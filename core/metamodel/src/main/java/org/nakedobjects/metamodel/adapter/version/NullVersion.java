package org.nakedobjects.metamodel.adapter.version;

import java.util.Date;

import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;


public class NullVersion implements Version {

	
    public boolean different(final Version version) {
        return false;
    }

    public Version next(final String user, final Date time) {
        throw new UnexpectedCallException();
    }

    public String getUser() {
        return "";
    }

    public Date getTime() {
        return new Date();
    }

    public String sequence() {
        return "";
    }
}
// Copyright (c) Naked Objects Group Ltd.
