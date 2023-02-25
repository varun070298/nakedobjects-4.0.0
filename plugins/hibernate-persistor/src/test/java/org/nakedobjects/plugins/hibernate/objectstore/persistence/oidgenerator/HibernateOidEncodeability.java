package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;


public class HibernateOidEncodeability {

    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    private DataOutputStreamExtended outputImpl;

    private HibernateOid oid;

    @Before
    public void setUp() throws IOException {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream(pipedInputStream);
        outputImpl = new DataOutputStreamExtended(pipedOutputStream);
    }

    @Test
    public void shouldBeAbleToEncodeHibernateOids() throws IOException {
        oid = HibernateOid.createTransient(java.lang.Object.class, 123);
        oid.encode(outputImpl);
    }

}

// Copyright (c) Naked Objects Group Ltd.
