package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.ObjectBorder;
import org.nakedobjects.plugins.dnd.viewer.border.SelectableViewAxis;
import org.nakedobjects.plugins.dnd.viewer.border.SelectedBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.lookup.OpenObjectDropDownBorder;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;


public class SubviewIconSpecification extends IconSpecification {
    
    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return super.canDisplay(content, requirement) && requirement.is(ViewRequirement.SUBVIEW);
    }

    @Override
    public View createView(final Content content, final ViewAxis axis) {
        final Icon icon = new Icon(content, this, axis);
        final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        icon.setTitle(new ObjectTitleText(icon, style));
        icon.setSelectedIcon(new IconGraphic(icon, style));

        View view = axis instanceof SelectableViewAxis ? new SelectedBorder(icon) : icon;

        if (content instanceof OneToOneField && ((OneToOneField) content).isEditable().isVetoed()) {
            return new ObjectBorder(view);
        } else {
            if (content.isOptionEnabled()) {
                return new ObjectBorder(new OpenObjectDropDownBorder(view));
            } else {
                return new ObjectBorder(view);
            }
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
