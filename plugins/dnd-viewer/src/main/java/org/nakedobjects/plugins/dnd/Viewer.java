package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.undo.UndoStack;


public interface Viewer {
    void markDamaged(final Bounds bounds);

    boolean hasFocus(final View view);

    UndoStack getUndoStack();
    
    Size getOverlaySize();

    void saveCurrentFieldEntry();

    void setKeyboardFocus(final View view);

    boolean isRunningAsExploration();

    void clearAction();

    /**
     * Force a repaint of the damaged area of the viewer.
     */
    void scheduleRepaint();

    void addToNotificationList(final View view);

    void removeFromNotificationList(final View view);

    void setBackground(Background background);

    InteractionSpy getSpy();

    void clearOverlayView();

    void clearOverlayView(final View view);

    void setOverlayView(final View view);

    void showInOverlay(Content content, Location location);

    // TODO should this be an extension?
    String selectFilePath(final String title, final String directory);

    void setClipboard(String clip, Class<?> class1);

    Object getClipboard(Class<?> class1);

    void disposeUnneededViews();
}
// Copyright (c) Naked Objects Group Ltd.
