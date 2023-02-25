package org.nakedobjects.runtime.transaction;

import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBrokerDefault;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifierDefault;

public class NakedObjectTransactionDefault extends NakedObjectTransactionAbstract {

    public NakedObjectTransactionDefault(
            final NakedObjectTransactionManager transactionManager) {
        this(transactionManager, new MessageBrokerDefault(), new UpdateNotifierDefault());
    }


    public NakedObjectTransactionDefault(
            NakedObjectTransactionManager transactionManager,
            MessageBroker messageBroker,
            UpdateNotifier updateNotifier) {
        super(transactionManager, messageBroker, updateNotifier);
    }


    @Override
    protected void doAbort() {}

    @Override
    protected void doFlush() {}


}


// Copyright (c) Naked Objects Group Ltd.
