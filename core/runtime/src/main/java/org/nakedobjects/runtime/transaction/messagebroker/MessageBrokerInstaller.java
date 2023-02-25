package org.nakedobjects.runtime.transaction.messagebroker;

import org.nakedobjects.metamodel.commons.component.Installer;


public interface MessageBrokerInstaller extends Installer {

	static String TYPE = "message-broker";

    MessageBroker createMessageBroker();
}

// Copyright (c) Naked Objects Group Ltd.
