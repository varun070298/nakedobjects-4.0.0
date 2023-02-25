package org.nakedobjects.runtime.transaction.messagebroker;

import java.util.List;

import org.nakedobjects.metamodel.commons.component.Noop;

public class MessageBrokerNoop implements MessageBroker, Noop {

    public void addMessage(String message) {}

    public void addWarning(String message) {}

    public void reset() {}

    public void ensureEmpty() {}

    public List<String> getMessages() {
        return null;
    }

    public String getMessagesCombined() {
        return null;
    }

    public List<String> getWarnings() {
        return null;
    }

    public String getWarningsCombined() {
        return null;
    }

}


// Copyright (c) Naked Objects Group Ltd.
