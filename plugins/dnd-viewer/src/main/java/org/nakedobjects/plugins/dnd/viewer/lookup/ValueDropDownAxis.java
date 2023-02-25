package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.View;


public class ValueDropDownAxis extends DropDownAxis {
    private final TextParseableContent content;

    public ValueDropDownAxis(final TextParseableContent content, final View originalView) {
        super(originalView);
        this.content = content;
    }

    @Override
    public void setSelection(final OptionContent selectedContent) {
        final NakedObject option = selectedContent.getNaked();
        content.parseTextEntry(option.titleString());
        content.entryComplete();
    }

}
// Copyright (c) Naked Objects Group Ltd.
