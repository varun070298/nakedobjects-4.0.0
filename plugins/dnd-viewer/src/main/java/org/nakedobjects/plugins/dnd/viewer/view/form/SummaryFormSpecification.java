package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ViewRequirement;


public class SummaryFormSpecification extends AbstractFormSpecification{

    protected int collectionRequirement() {
        return ViewRequirement.NONE;
    }
    
    protected boolean include(Content content, int sequence) {
        return sequence < 4 && content.getNaked() != null;
    }
    
    public String getName() {
        return "Summary form";
    }
}
// Copyright (c) Naked Objects Group Ltd.
