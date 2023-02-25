package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.BackgroundBorder;
import org.nakedobjects.plugins.dnd.viewer.border.DisposeOverlay;
import org.nakedobjects.plugins.dnd.viewer.border.LineBorder;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;


class DropDownObjectOverlaySpecification extends AbstractCompositeViewSpecification implements SubviewSpec {
    public DropDownObjectOverlaySpecification() {
        builder = new StackLayout(new DropDownListBuilder(this), true);
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        throw new UnexpectedCallException();
    }

    // TODO make this class abstract and create subclass (DropDownObjectObjectOverlaySepecification) with this
    // method
    public View createSubview(final Content content, final ViewAxis lookupAxis, int fieldNumber) {
        final Icon icon = new Icon(content, this, lookupAxis);
        final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        icon.setTitle(new ObjectTitleText(icon, style));
        icon.setSelectedIcon(new IconGraphic(icon, style));
        return new DropDownSelection(icon);
    }
    
    @Override
    protected View decorateView(View view) {
        View list = new DropDownFocusBorder(view);
        return new DisposeOverlay(new BackgroundBorder(new LineBorder(new ScrollBorder(list))));
    }

    public String getName() {
        return "Object Drop Down Overlay";
    }
}

// Copyright (c) Naked Objects Group Ltd.
