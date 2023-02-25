package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;


@RunWith(Parameterized.class)
public class HibernateOidDecodeability {

    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    private DataOutputStreamExtended outputImpl;
    private DataInputStreamExtended inputImpl;

    private HibernateOid oid, oid2;

    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][] { { java.lang.Object.class, 123, -1 }, { java.lang.Object.class, 123, 456 }, });
    }

    private final Class<?> clazz;
    private final Long primaryKey;
    private final Long hibernateId;

    public HibernateOidDecodeability(final Class<?> clazz, final long primaryKey, final long hibernateId) {
        this.clazz = clazz;
        this.primaryKey = primaryKey;
        this.hibernateId = hibernateId;
    }

    @Before
    public void setUp() throws IOException {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream(pipedInputStream);
        outputImpl = new DataOutputStreamExtended(pipedOutputStream);
        inputImpl = new DataInputStreamExtended(pipedInputStream);
    }

    @Test
    public void shouldBeAbleToDecodeHibernateOids() throws IOException {
        oid = HibernateOid.createTransient(clazz, primaryKey);
        if (hibernateId != -1) {
            oid.setHibernateId(hibernateId);
            oid.makePersistent();
        }
        oid.encode(outputImpl);

        oid2 = new HibernateOid(inputImpl);

        assertThat(oid2.getClassName(), is(equalTo(oid.getClassName())));
        assertThat(oid2.getHibernateId(), is(equalTo(oid.getHibernateId())));
        assertThat(oid2.getPrevious(), is(equalTo(oid.getPrevious())));
        assertThat(oid2.getPrimaryKey(), is(equalTo(oid.getPrimaryKey())));
        assertThat(oid2.isTransient(), is(equalTo(oid.isTransient())));
    }

}

// Copyright (c) Naked Objects Group Ltd.
