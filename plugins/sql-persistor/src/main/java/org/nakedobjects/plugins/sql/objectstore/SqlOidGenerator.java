package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGeneratorAbstract;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class SqlOidGenerator extends OidGeneratorAbstract {
    private long number;
    private long transientNumber;
    private final DatabaseConnectorPool connectionPool;

    public SqlOidGenerator(final DatabaseConnectorPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void open() {
        transientNumber = -9999999;

        if (connectionPool != null) {
            DatabaseConnector db = connectionPool.acquire();
            try {
                db.begin();
                if (!db.hasTable("NO_SERIAL_ID")) {
                    db.update("create table NO_SERIAL_ID (NUMBER INTEGER)");
                    db.update("insert into NO_SERIAL_ID values (1)");
                }

                Results rs = db.select("select NUMBER from NO_SERIAL_ID");
                rs.next();
                number = rs.getLong("NUMBER");
                rs.close();
                db.commit();
            } catch (ObjectPersistenceException e) {
                db.rollback();
                throw e;
            } finally {
                connectionPool.release(db);
            }
        }
    }

    public String name() {
        return "Sql Oids";
    }

    public void close() {
        if (connectionPool != null) {
            DatabaseConnector db = connectionPool.acquire();
            try {
                db.begin();
                if (db.update("update NO_SERIAL_ID set NUMBER = " + number) != 1) {
                    throw new SqlObjectStoreException("failed to update serial id table; no rows updated");
                }
                db.commit();
            } catch (ObjectPersistenceException e) {
                throw e;
            } finally {
                if (db != null) {
                    connectionPool.release(db);
                }
            }
        }
    }

    public SqlOid createTransientOid(final Object object) {
        String className = object.getClass().getName();
        return SqlOid.createTransient(className, transientNumber++);
    }

    public void convertTransientToPersistentOid(final Oid oid) {
        Assert.assertNotNull("No connection set up", connectionPool);
        IntegerPrimaryKey primaryKey = new IntegerPrimaryKey((int) number++);
        ((SqlOid) oid).setId(primaryKey);
        ((SqlOid) oid).makePersistent();
    }

    public void convertPersistentToTransientOid(final Oid oid) {}

    public void debugData(final DebugString debug) {
        debug.appendln(this.toString());
        debug.indent();
        debug.appendln("id", number);
        debug.appendln("transient id", transientNumber);
        debug.unindent();
    }

    public String debugTitle() {
        return "SQL OID Generator";
    }
}
// Copyright (c) Naked Objects Group Ltd.
