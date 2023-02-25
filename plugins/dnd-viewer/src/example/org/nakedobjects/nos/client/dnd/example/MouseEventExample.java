package org.nakedobjects.viewer.dnd.example;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MouseEventExample extends Frame {

    public static void main(final String[] args) {
        final MouseEventExample frame = new MouseEventExample();
        frame.setSize(300, 400);
        frame.show();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                frame.dispose();
            }
        });

        frame.addMouseListener(new Mouse());
    }

    int left = 10 + getInsets().left;
    int top = 40 + getInsets().top;
    int width = 100;
    int height;

    public void show() {
        super.show();
        left = 10 + getInsets().left;
        top = 10 + getInsets().top;
    }
}

class Mouse implements MouseListener {

    public void mouseClicked(MouseEvent e) {
        System.out.print(e.isPopupTrigger() ? "POPUP " : "");
        System.out.println(e);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        System.out.print(e.isPopupTrigger() ? "POPUP " : "");
        System.out.println(e);
    }

    public void mouseReleased(MouseEvent e) {
        System.out.print(e.isPopupTrigger() ? "POPUP " : "");
        System.out.println(e);
    }

}
// Copyright (c) Naked Objects Group Ltd.
