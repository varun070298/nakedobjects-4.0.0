package org.nakedobjects.plugins.dnd.viewer.undo;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


public class SetValueCommand implements Command {
    private final String description;
    private final OneToOneAssociation value;
    private final NakedObject object;
    private final String oldValue;

    public SetValueCommand(final NakedObject object, final OneToOneAssociation value) {
        final EncodableFacet facet = value.getFacet(EncodableFacet.class);
        this.oldValue = facet.toEncodedString(object);
        this.object = object;
        this.value = value;

        this.description = "reset the value to " + oldValue;
    }

    public String getDescription() {
        return description;
    }

    public void undo() {
        final EncodableFacet facet = value.getFacet(EncodableFacet.class);
        final Object obj = facet.fromEncodedString(oldValue);
        final NakedObject adapter = getAdapterManager().adapterFor(obj);
        value.setAssociation(object, adapter);
        // have commented this out because it isn't needed; the transaction manager will do this
        // for us on endTransaction.  Still, if I'm wrong and it is needed, hopefully this
        // comment will help...
        // NakedObjectsContext.getObjectPersistor().objectChangedAllDirty();
    }

    public void execute() {}

    public String getName() {
        return "entry";
    }
    
    
    // //////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // //////////////////////////////////////////////////////////////////

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }


}
// Copyright (c) Naked Objects Group Ltd.
