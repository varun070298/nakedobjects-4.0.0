package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.View;


public class IconizeWindowControl extends WindowControl {
    private static IconizeWindowRender render = new IconizeWindow3DRender();
//    private static IconizeWindowRender render = new IconizeWindowSimpleRender();
    
    public IconizeWindowControl(final View target) {
        super(new IconizeViewOption(), target);
    }

    @Override
    public void draw(final Canvas canvas) {
        render.draw(canvas, WIDTH, HEIGHT, false, isOver(), isPressed());
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
