package org.nakedobjects.runtime.transaction.updatenotifier;

import org.apache.log4j.Logger;


public abstract class UpdateNotifierAbstract implements UpdateNotifier {
	
    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UpdateNotifierAbstract.class);

    
    ////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////

    public UpdateNotifierAbstract() {
        // does nothing
    }
    

    ////////////////////////////////////////////////////
    // Injectable
    ////////////////////////////////////////////////////

    /**
     * Injects.
     */
    public void injectInto(Object candidate) {
        if (UpdateNotifierAware.class.isAssignableFrom(candidate.getClass())) {
        	UpdateNotifierAware cast = UpdateNotifierAware.class.cast(candidate);
            cast.setUpdateNotifier(this);
        }
    }
    

}

// Copyright (c) Naked Objects Group Ltd.
