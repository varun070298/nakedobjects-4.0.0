package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.runtime.transaction.NakedObjectTransactionAbstract;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;



public class SqlExecutionContext extends NakedObjectTransactionAbstract {
    private final DatabaseConnector connection;

    public SqlExecutionContext(final DatabaseConnector connection, NakedObjectTransactionManager transactionManager, MessageBroker messageBroker, UpdateNotifier updateNotifier) {
        super(transactionManager, messageBroker, updateNotifier);
        this.connection = connection;
    }

    public DatabaseConnector getConnection() {
        return connection;
    }

    protected void doAbort() {}

    protected void doFlush() {}

}
// Copyright (c) Naked Objects Group Ltd.
