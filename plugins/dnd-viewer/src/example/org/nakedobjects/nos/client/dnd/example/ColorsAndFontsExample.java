package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.image.java.AwtTemplateImageLoaderInstaller;
import org.nakedobjects.nof.testsystem.TestProxyConfiguration;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.viewer.dnd.Toolkit;
import org.nakedobjects.viewer.dnd.drawing.Color;
import org.nakedobjects.viewer.dnd.drawing.Text;
import org.nakedobjects.viewer.dnd.image.ImageFactory;
import org.nakedobjects.viewer.dnd.viewer.AwtColor;
import org.nakedobjects.viewer.dnd.viewer.AwtText;
import org.nakedobjects.viewer.dnd.viewer.AwtToolkit;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;


public class ColorsAndFontsExample extends Frame {
    private static final int SPACE = 22;
    private Color[] colors;
    private Text[] fonts;

    public ColorsAndFontsExample(final String title) {
        super(title);

        colors = new Color[] { Toolkit.getColor("black"), Toolkit.getColor("white"), Toolkit.getColor("primary1"),
                Toolkit.getColor("primary2"), Toolkit.getColor("primary3"), Toolkit.getColor("secondary1"),
                Toolkit.getColor("secondary2"), Toolkit.getColor("secondary3"),

                Toolkit.getColor("background.application"), Toolkit.getColor("background.window"),
                Toolkit.getColor("background.content-menu"), Toolkit.getColor("background.value-menu"),
                Toolkit.getColor("background.view-menu"), Toolkit.getColor("background.workspace-menu"),

                Toolkit.getColor("menu.normal"), Toolkit.getColor("menu.disabled"), Toolkit.getColor("menu.reversed"),

                Toolkit.getColor("text.edit"), Toolkit.getColor("text.cursor"), Toolkit.getColor("text.highlight"),
                Toolkit.getColor("text.saved"),

                Toolkit.getColor("identified"), Toolkit.getColor("invalid"), Toolkit.getColor("out-of-sync"),
                Toolkit.getColor("error"), Toolkit.getColor("valid"), Toolkit.getColor("active"), };

        fonts = new Text[] { Toolkit.getText(ColorsAndFonts.TEXT_ICON), Toolkit.getText(ColorsAndFonts.TEXT_CONTROL),
                Toolkit.getText(ColorsAndFonts.TEXT_TITLE_SMALL), Toolkit.getText(ColorsAndFonts.TEXT_LABEL),
                Toolkit.getText(ColorsAndFonts.TEXT_MENU), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL),
                Toolkit.getText(ColorsAndFonts.TEXT_STATUS), Toolkit.getText(ColorsAndFonts.TEXT_TITLE) };
    }

    public static void main(final String[] args) {
        NakedObjectsContext.setConfiguration(new TestProxyConfiguration());

        new ImageFactory(new AwtTemplateImageLoaderInstaller().createLoader());
        new AwtToolkit();

        final ColorsAndFontsExample f = new ColorsAndFontsExample("Colors and Fonts");
        f.setSize(800, 600);
        f.show();

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(final java.awt.event.WindowEvent e) {
                f.dispose();
            }
        });
    }

    public Insets getInsets() {
        return new Insets(30, 10, 30, 10);
    }

    public void paint(final Graphics g) {
        int x = 10;

        for (int i = 0; i < colors.length; i++) {
            g.setColor(java.awt.Color.black);
            g.drawString(colors[i].getName(), x, 50 + SPACE * i);

            g.setColor(((AwtColor) colors[i]).getAwtColor());
            g.fillRect(x + 200, 40 + SPACE * i, 40, 12);
        }

        x += 300;
        g.setColor(java.awt.Color.black);
        for (int i = 0; i < fonts.length; i++) {
            g.setFont(((AwtText) fonts[i]).getAwtFont());
            g.drawString(fonts[i].getName(), x, 50 + SPACE * i);
            g.drawString("Abcdefghijkl", x + 200, 50 + SPACE * i);

        }

    }
}
// Copyright (c) Naked Objects Group Ltd.
