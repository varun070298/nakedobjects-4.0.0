package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public interface RenderingArea {

    Dimension getSize();

    Image createImage(int w, int h);

    Insets getInsets();

    void repaint();

    void repaint(int x, int y, int width, int height);

    void setCursor(Cursor cursor);

    void dispose();

    void setBounds(int i, int j, int k, int l);

    void show();

    void addMouseMotionListener(MouseMotionListener interactionHandler);

    void addMouseListener(MouseListener interactionHandler);

    void addKeyListener(KeyListener interactionHandler);

    String selectFilePath(String title, String directory);
}
// Copyright (c) Naked Objects Group Ltd.
