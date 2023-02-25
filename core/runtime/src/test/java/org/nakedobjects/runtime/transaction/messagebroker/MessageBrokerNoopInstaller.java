package org.nakedobjects.runtime.transaction.messagebroker;

import org.nakedobjects.runtime.installers.InstallerAbstract;


public class MessageBrokerNoopInstaller extends InstallerAbstract implements MessageBrokerInstaller {

	
    public MessageBrokerNoopInstaller() {
		super(MessageBrokerInstaller.TYPE, "noop");
	}

    public MessageBroker createMessageBroker() {
    	return new MessageBrokerNoop();
    }
    
}


// Copyright (c) Naked Objects Group Ltd.
