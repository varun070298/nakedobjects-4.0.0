package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;


class EmptyFieldTitleText extends TitleText {
    private final Content content;

    public EmptyFieldTitleText(final View view, final Text style) {
        super(view, style, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        content = view.getContent();
    }

    @Override
    protected String title() {
        return ((ObjectContent) content).getSpecification().getSingularName();
    }

}
// Copyright (c) Naked Objects Group Ltd.
