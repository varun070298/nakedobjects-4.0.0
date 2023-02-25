package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.View;


public class CloseWindowControl extends WindowControl {
    private static CloseWindowRender render = new CloseWindow3DRender();
//    private static CloseWindowRender render = new CloseWindowSimpleRender();

    public CloseWindowControl(final View target) {
        super(new CloseViewOption(), target);
    }

    @Override
    public void draw(final Canvas canvas) {
        render.draw(canvas, WIDTH, HEIGHT, action.disabled(this).isVetoed(), isOver(), isPressed());
    }
}
// Copyright (c) Naked Objects Group Ltd.
