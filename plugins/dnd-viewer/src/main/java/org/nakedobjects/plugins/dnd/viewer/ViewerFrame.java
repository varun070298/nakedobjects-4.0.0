package org.nakedobjects.plugins.dnd.viewer;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;


public class ViewerFrame extends Frame implements RenderingArea {
    private static final String DEFAULT_TITLE = "Naked Objects";
    private static final long serialVersionUID = 1L;
    private XViewer viewer;

    public ViewerFrame() {}

    {
        setBackground(((AwtColor) Toolkit.getColor(ColorsAndFonts.COLOR_APPLICATION)).getAwtColor());

        AWTUtilities.addWindowIcon(this, "application-logo.png");
        setTitle(null);

        /*
         * compensate for change in tab handling in Java 1.4
         */
        try {
            final Class c = getClass();
            final Method m = c.getMethod("setFocusTraversalKeysEnabled", new Class[] { Boolean.TYPE });
            m.invoke(this, new Object[] { Boolean.FALSE });
        } catch (final SecurityException e1) {
            e1.printStackTrace();
        } catch (final NoSuchMethodException ignore) {
            /*
             * Ignore no such method exception as this method is only available, but needed, in version 1.4
             * and later.
             */
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean imageUpdate( Image img, int flags, int x, int y, int w, int h ) {
            repaint();
            return true;
    }
    
    /**
     * Calls <code>update()</code> to do double-buffered drawing of all views.
     * 
     * @see #update(Graphics)
     * @see java.awt.Component#paint(Graphics)
     */
    @Override
    public final void paint(final Graphics g) {
        update(g);
    }

    public void quit() {
        viewer.quit();
    }

    /**
     * Paints the double-buffered image. Calls the <code>draw()</code> method on each top-level view.
     * 
     * @see java.awt.Component#update(Graphics)
     */
    @Override
    public void update(final Graphics g) {
        viewer.paint(g);
    }

    public void setViewer(final XViewer viewer) {
        this.viewer = viewer;
    }

    public void init() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                new ShutdownDialog(ViewerFrame.this);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                ViewerFrame.this.viewer.sizeChange();
            }
        });
    }

    /**
     * Expose as a .NET property
     * 
     * @property
     */
    public void set_Viewer(final XViewer viewer) {
        setViewer(viewer);
    }

    /**
     * Expose as a .NET property
     * 
     * @property
     */
    public void set_Title(final String title) {
        setTitle(title);
    }

    @Override
    public void setTitle(final String title) {
        final String application = AboutNakedObjects.getApplicationName();
        final String str = title == null ? (application == null ? DEFAULT_TITLE : application) : title;
        super.setTitle(str);
    }

    public String selectFilePath(final String title, final String directory) {
        final FileDialog dlg = new FileDialog(this, title);
        dlg.setVisible(true);

        final String path = dlg.getDirectory() + dlg.getFile();
        return path;
    }
}
// Copyright (c) Naked Objects Group Ltd.
