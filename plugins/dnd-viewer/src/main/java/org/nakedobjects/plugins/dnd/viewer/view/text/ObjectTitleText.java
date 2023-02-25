package org.nakedobjects.plugins.dnd.viewer.view.text;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class ObjectTitleText extends TitleText {
    private final Content content;

    public ObjectTitleText(final View view, final Text style) {
        super(view, style, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
        content = view.getContent();
    }

    @Override
    protected String title() {
        return content.title();
    }

}
// Copyright (c) Naked Objects Group Ltd.
