package org.nakedobjects.viewer.dnd.viewer;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;



public class IconImage extends Frame {

    public IconImage(String fileName) {
        Image image = getToolkit().getImage(fileName);
        setIconImage(image);
        setSize(150, 150);
        show();
    }

    public static void main(String[] args) {
        new IconImage(args[0]);
    }

    public void paint(Graphics g) {
        Insets in = insets();
        g.drawImage(getIconImage(), in.left, in.top, this);
    }
}


// Copyright (c) Naked Objects Group Ltd.
