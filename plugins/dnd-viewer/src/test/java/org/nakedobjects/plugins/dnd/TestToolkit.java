package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.Background;
import org.nakedobjects.plugins.dnd.BackgroundTask;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Feedback;
import org.nakedobjects.plugins.dnd.InteractionSpy;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Viewer;
import org.nakedobjects.plugins.dnd.viewer.basic.NullColor;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.DummyText;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.undo.UndoStack;


public class TestToolkit extends Toolkit {

    public static void createInstance() {
        if (getInstance() == null) {
            new TestToolkit();
        }
    }

    private TestToolkit() {}

    @Override
    protected void init() {
        colorsAndFonts = new ColorsAndFonts() {

            public int defaultBaseline() {
                return 0;
            }

            public int defaultFieldHeight() {
                return 0;
            }

            public Color getColor(final int rgbColor) {
                return null;
            }

            public Color getColor(final String name) {
                return new NullColor();
            }

            public Text getText(final String name) {
                return new DummyText();
            }

            public void init() {}
        };

        viewer = new Viewer() {

            public void addToNotificationList(final View view) {}

            public void clearAction() {}

            public void clearOverlayView() {}

            public void clearOverlayView(final View view) {}

            public void disposeUnneededViews() {}

            public Object getClipboard(final Class<?> class1) {
                return null;
            }

            public InteractionSpy getSpy() {
                return null;
            }

            UndoStack undoStack = new UndoStack();

            public UndoStack getUndoStack() {
                return undoStack;
            }

            public boolean hasFocus(final View view) {
                return false;
            }

            public boolean isRunningAsExploration() {
                return false;
            }

            public void markDamaged(final Bounds bounds) {}

            public void removeFromNotificationList(final View view) {}

            public void scheduleRepaint() {}

            public void saveCurrentFieldEntry() {}

            public String selectFilePath(final String title, final String directory) {
                return null;
            }

            public void setBackground(final Background background) {}

            public void setClipboard(final String clip, final Class<?> class1) {}

            public void setKeyboardFocus(final View view) {}

            public void setOverlayView(final View view) {}

            public void showInOverlay(Content content, Location location) {}

            public Size getOverlaySize() {
                return null;
            }

        };

        feedbackManager = new Feedback() {

            public void showArrowCursor() {}

            public void showCrosshairCursor() {}

            public void showDefaultCursor() {}

            public void showException(final Throwable e) {}

            public void showHandCursor() {}

            public void showMoveCursor() {}

            public void showResizeDownCursor() {}

            public void showResizeDownLeftCursor() {}

            public void showResizeDownRightCursor() {}

            public void showResizeLeftCursor() {}

            public void showResizeRightCursor() {}

            public void showResizeUpCursor() {}

            public void showResizeUpLeftCursor() {}

            public void showResizeUpRightCursor() {}

            public void showTextCursor() {}

            public void addMessage(final String string) {}

            public void clearAction() {}

            public void clearBusy(final View view) {}

            public void clearError() {}

            public String getStatusBarOutput() {
                return null;
            }

            public boolean isBusy(final View view) {
                return false;
            }

            public void setAction(final String string) {}

            public void setBusy(final View view, final BackgroundTask task) {}

            public void setError(final String string) {}

            public void setViewDetail(final String string) {}

            public void showBusyState(final View view) {}

            public void showMessagesAndWarnings() {}
        };
    }
}

// Copyright (c) Naked Objects Group Ltd.
