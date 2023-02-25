package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;


public abstract class AbstractValueOption extends AbstractUserAction {
    protected final AbstractField field;

    AbstractValueOption(AbstractField field, final String name) {
        super(name);
        this.field = field;
    }

    protected NakedObject getValue(final View view) {
        final TextParseableContent vc = (TextParseableContent) view.getContent();
        final NakedObject value = vc.getNaked();
        return value;
    }

    protected void updateParent(final View view) {
        // have commented this out because it isn't needed; the transaction manager will do this
        // for us on endTransaction.  Still, if I'm wrong and it is needed, hopefully this
        // comment will help...
        // NakedObjectsContext.getObjectPersistor().objectChangedAllDirty();
        
        view.markDamaged();
        view.getParent().invalidateContent();
    }

    
    protected boolean isEmpty(final View view) {
        return field.isEmpty();
   }
}
// Copyright (c) Naked Objects Group Ltd.
