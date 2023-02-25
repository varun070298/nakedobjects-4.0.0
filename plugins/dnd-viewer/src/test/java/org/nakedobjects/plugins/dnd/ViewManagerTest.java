package org.nakedobjects.plugins.dnd;

import junit.framework.TestCase;
import junit.textui.TestRunner;


public class ViewManagerTest extends TestCase {
    // private final static Component COMPONENT = new Component() {
    // };

    public static void main(final String[] args) {
        TestRunner.run(ViewManagerTest.class);
    }

    public void testNone() {}

    /*
     * public void testMouseClick() { MockWorkspace workspace = new MockWorkspace(null); MockView view = new
     * MockView(); workspace.setupIdentifyView(view);
     * 
     * ViewManager manager = new ViewManager(workspace, null);
     * 
     * view.setupGetAbsoluteLocation(new Location(10, 10)); view.setupIndicatesForView(true);
     * view.setExpectedFirstClickCalls(1);
     * 
     * manager.mouseClicked(createMouseEvent(10, 20, 1, MouseEvent.BUTTON1_MASK));
     * 
     * view.verify(); }
     * 
     * public void testPopupMouseClick() { MockWorkspace workspace = new MockWorkspace(null); MockView view =
     * new MockView(); workspace.setupIdentifyView(view);
     * 
     * MockPopupMenu popup = new MockPopupMenu(); ViewManager manager = new ViewManager(workspace, popup);
     * 
     * view.setupGetAbsoluteLocation(new Location(10, 10)); view.setupIndicatesForView(true);
     * 
     * workspace.addExpectedSetIdentifiedViewValues(view);
     * 
     * popup.addExpectedInitValues(view, true, true); view.setupGetAbsoluteLocation(new Location(10, 10));
     * view.setupIndicatesForView(true); // popup.setupGetParent(null); // popup.setupIndicatesForView(true);
     * 
     * manager.mouseClicked(createMouseEvent(10, 20, 1, MouseEvent.BUTTON3_MASK));
     * 
     * popup.verify(); view.verify();
     * 
     * popup.setExpectedFirstClickCalls(1); }
     * 
     * private MouseEvent createMouseEvent(final int x, final int y, final int count, final int button) {
     * return new MouseEvent(COMPONENT, 0, 0, button, x, y, count, false); }
     */
}
// Copyright (c) Naked Objects Group Ltd.
