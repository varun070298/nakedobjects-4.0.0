package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public abstract class Toolkit {
    public static boolean debug = false;
    private static Toolkit instance;

    public static int defaultBaseline() {
        return getInstance().colorsAndFonts.defaultBaseline();
    }

    public static int defaultFieldHeight() {
        return getInstance().colorsAndFonts.defaultFieldHeight();
    }

    public static Color getColor(final int rgbColor) {
        return instance.colorsAndFonts.getColor(rgbColor);
    }

    public static Color getColor(final String name) {
        final Color color = getInstance().colorsAndFonts.getColor(name);
        if (color == null) {
            throw new NakedObjectException("No such color: " + name);
        }
        return color;
    }

    public static ContentFactory getContentFactory() {
        return getInstance().contentFactory;
    }

    protected static Toolkit getInstance() {
        return instance;
    }

    public static Text getText(final String name) {
        final Text text = getInstance().colorsAndFonts.getText(name);
        if (text == null) {
            throw new NakedObjectException("No such text style: " + name);
        }
        return text;
    }

    public static Viewer getViewer() {
        return getInstance().viewer;
    }

    public static Feedback getFeedbackManager() {
        return getInstance().feedbackManager;
    }

    public static ViewFactory getViewFactory() {
        return getInstance().viewFactory;
    }

    protected ContentFactory contentFactory;
    protected ColorsAndFonts colorsAndFonts;
    protected Viewer viewer;
    protected Feedback feedbackManager;
    protected ViewFactory viewFactory;

    protected Toolkit() {
        if (instance != null) {
            throw new IllegalStateException("Toolkit already instantiated");
        }
        instance = this;
        init();
    }

    protected abstract void init();

}

// Copyright (c) Naked Objects Group Ltd.
