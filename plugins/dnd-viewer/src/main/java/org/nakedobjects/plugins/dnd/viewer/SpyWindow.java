package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.nakedobjects.plugins.dnd.InteractionSpyWindow;


class SpyWindow implements InteractionSpyWindow {
    private int event;
    private String label[][] = new String[2][20];
    private String[] trace = new String[60];
    private int traceIndex;
    private SpyFrame frame;

    class SpyFrame extends Frame {
        private static final long serialVersionUID = 1L;

        public SpyFrame() {
            super("View/Interaction Spy");
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(final WindowEvent e) {
                    close();
                }
            });
        }

        @Override
        public void paint(final Graphics g) {
            int baseline = getInsets().top + 15;

            g.drawString("Event " + event, 10, baseline);
            baseline += 18;

            for (int i = 0; i < label[0].length; i++) {
                if (label[0][i] != null) {
                    g.drawString(label[0][i], 10, baseline);
                    g.drawString(label[1][i], 150, baseline);
                }
                baseline += 12;
            }

            baseline += 6;
            for (int i = 0; i < traceIndex; i++) {
                if (trace[i] != null) {
                    g.drawString(trace[i], 10, baseline);
                }
                baseline += 12;
            }
        }
    }

    public void display(final int event, final String label[][], final String[] trace, final int traceIndex) {
        if (frame != null) {
            this.event = event;
            this.traceIndex = traceIndex;
            this.label = label;
            this.trace = trace;
            frame.repaint();
        }
    }

    public void open() {
        frame = new SpyFrame();
        frame.setBounds(10, 10, 800, 500);
        frame.setVisible(true);
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }
}

// Copyright (c) Naked Objects Group Ltd.
