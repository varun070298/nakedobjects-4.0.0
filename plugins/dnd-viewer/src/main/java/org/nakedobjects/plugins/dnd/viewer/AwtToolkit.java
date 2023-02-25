package org.nakedobjects.plugins.dnd.viewer;

import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.basic.LogoBackground;


public class AwtToolkit extends Toolkit {
    @Override
    protected void init() {
        final XViewer v = new XViewer();
        final XFeedbackManager f = new XFeedbackManager(v);
        v.setFeedbackManager(f);
        feedbackManager = f;
        viewer = v;
        viewer.setBackground(new LogoBackground());
        contentFactory = new DefaultContentFactory();
        viewFactory = new SkylarkViewFactory();
        colorsAndFonts = new AwtColorsAndFonts();

        colorsAndFonts.init();
    }
}
// Copyright (c) Naked Objects Group Ltd.
