package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ResizeWindowControl extends WindowControl {
    private static ResizeWindowRender render = new ResizeWindow3DRender();
    // private static ResizeWindowRender render = new ResizeWindowSimpleRender();
    
    public ResizeWindowControl(final View target) {
        super(new UserAction() {

            public Consent disabled(final View view) {
                return Veto.DEFAULT;
            }

            public void execute(final Workspace workspace, final View view, final Location at) {}

            public String getDescription(final View view) {
                return "";
            }

            public String getHelp(final View view) {
                return "";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public String getName(final View view) {
                return "Resize";
            }
        }, target);

    }

    @Override
    public void draw(final Canvas canvas) {
       render.draw(canvas, WIDTH, HEIGHT, action.disabled(this).isVetoed(), isOver(), isPressed());
    }
}
// Copyright (c) Naked Objects Group Ltd.
