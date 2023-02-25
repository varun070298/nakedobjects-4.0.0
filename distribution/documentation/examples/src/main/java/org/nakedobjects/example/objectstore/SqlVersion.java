package org.nakedobjects.example.objectstore;

import java.util.Date;

import org.nakedobjects.metamodel.adapter.version.Version;

public class SqlVersion implements Version {
	
    private int sequence;

    public SqlVersion(String user) {
        sequence = 1;
    }

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////


    public boolean different(Version version) {
        return !version.sequence().equals(sequence());
    }

    public Date getTime() {
        return new Date();
    }

    public String getUser() {
        return "";
    }

    public String sequence() {
        return "" + sequence;
    }

}


// Copyright (c) Naked Objects Group Ltd.
