package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;


public class InternalFormSpecification extends WindowFormSpecification {

    @Override
    protected View decorateView(final View formView) {
        return new ScrollBorder(super.decorateView(formView));
    }
}
// Copyright (c) Naked Objects Group Ltd.
