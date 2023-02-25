package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.plugins.sql.objectstore.IntegerPrimaryKey;
import org.nakedobjects.plugins.sql.objectstore.SqlOid;
import org.nakedobjects.plugins.sql.objectstore.SqlOid.State;

import junit.framework.TestCase;


public class SqlOidTest extends TestCase {

    /*
     * Test method for 'org.nakedobjects.persistence.sql.SqlOid.hashCode()'
     */
    public void testHashCode() {
        SqlOid oid1 = new SqlOid("className", new IntegerPrimaryKey(13), State.TRANSIENT);
        SqlOid oid2 = new SqlOid("className", new IntegerPrimaryKey(13), State.TRANSIENT);

        assertEquals(oid1.hashCode(), oid2.hashCode());
    }

    /*
     * Test method for 'org.nakedobjects.persistence.sql.SqlOid.copyFrom(Oid)'
     */
    public void testCopyFrom() {

    }

}
// Copyright (c) Naked Objects Group Ltd.
