package org.nakedobjects.plugins.html.action.edit;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;



public class Save implements Action {

    public void execute(final Request request, final Context context, final Page page) {
    	
        final NakedObject adapter = context.getMappedObject(request.getObjectId());
        
        // xactn mgmt now done by PersistenceSession#makePersistent()
        // getTransactionManager().startTransaction();
        getPersistenceSession().makePersistent(adapter);
        // getTransactionManager().endTransaction();
        
        // return to view
        request.forward(ForwardRequest.viewObject(request.getObjectId()));
    }

    
    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    public String name() {
        return "save";
    }

}

// Copyright (c) Naked Objects Group Ltd.
