package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;


public class ObjectDropDownAxis extends DropDownAxis {
    private final ObjectContent content;

    public ObjectDropDownAxis(final ObjectContent content, final View originalView) {
        super(originalView);
        this.content = content;
    }

    @Override
    public void setSelection(final OptionContent selectedContent) {
        final NakedObject option = selectedContent.getNaked();
        content.setObject(option);
    }

}
// Copyright (c) Naked Objects Group Ltd.
