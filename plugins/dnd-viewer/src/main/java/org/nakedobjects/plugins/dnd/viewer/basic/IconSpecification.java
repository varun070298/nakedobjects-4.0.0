package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.border.ObjectBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;


public abstract class IconSpecification implements ViewSpecification {
    private final boolean isSubView;
    private final boolean isReplaceable;

    public IconSpecification() {
        this(true, true);
    }

    IconSpecification(final boolean isSubView, final boolean isReplaceable) {
        this.isSubView = isSubView;
        this.isReplaceable = isReplaceable;
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return requirement.isClosed() && content.isObject() && content.getNaked() != null;
    }
    
    public View createView(final Content content, final ViewAxis axis) {
        final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        final Icon icon = new Icon(content, this, axis);
        icon.setTitle(new ObjectTitleText(icon, style));
        icon.setSelectedIcon(new IconGraphic(icon, style));
        return new ObjectBorder(new IconOpenAction(icon));
    }

    public String getName() {
        return "Icon";
    }

    public boolean isSubView() {
        return isSubView;
    }

    public boolean isReplaceable() {
        return isReplaceable;
    }
    
    public boolean isResizeable() {
        return false;
    }

    public View decorateSubview(final View subview) {
        return subview;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isAligned() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
