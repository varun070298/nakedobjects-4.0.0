package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;

import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class PrintOption extends AbstractUserAction {
    private final int HEIGHT = 60;
    private final int LEFT = 60;

    public PrintOption() {
        super("Print...");
    }

    @Override
    public Consent disabled(final View component) {
        return Allow.DEFAULT;
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final Frame frame = new Frame();
        final PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(frame, "Print object", null);

        if (job != null) {
            final Graphics pg = job.getGraphics();
            final Dimension pageSize = job.getPageDimension();

            if (pg != null) {
                pg.translate(LEFT, HEIGHT);
                pg.drawRect(0, 0, pageSize.width - LEFT - 1, pageSize.height - HEIGHT - 1);
                view.print(new PrintCanvas(pg, view));
                pg.dispose();
            }

            job.end();
        }
        frame.dispose();
    }

    private class PrintCanvas extends AwtCanvas {
        PrintCanvas(final Graphics g, final View view) {
            super(g, null, 0, 0, view.getSize().getWidth(), view.getSize().getHeight());
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
