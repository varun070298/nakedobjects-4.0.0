package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractViewDecorator;
import org.nakedobjects.plugins.dnd.viewer.border.ObjectBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;


class IconOpenAction extends AbstractViewDecorator {
    protected IconOpenAction(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    public void viewMenuOptions(final UserActionSet menuOptions) {
        super.viewMenuOptions(menuOptions);
        menuOptions.add(new AbstractUserAction("Close") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                getView().dispose();
                // getWorkspace().removeObject((NakedObject) view.getContent().getNaked());
            }
        });
    }

    private void openIcon() {
        getWorkspace().addWindowFor(getContent().getNaked(), getLocation());
    }

    @Override
    public void secondClick(final Click click) {
        openIcon();
    }
}

public class RootIconSpecification extends IconSpecification {
    
    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return super.canDisplay(content, requirement) && requirement.is(ViewRequirement.ROOT);
    }
    
    @Override
    public View createView(final Content content, final ViewAxis axis) {
        final Icon icon = new Icon(content, this, axis);
        final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        icon.setTitle(new ObjectTitleText(icon, style));
        icon.setSelectedIcon(new IconGraphic(icon, style));
        return new ObjectBorder(new IconOpenAction(icon));
    }
    
    public boolean isReplaceable() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
