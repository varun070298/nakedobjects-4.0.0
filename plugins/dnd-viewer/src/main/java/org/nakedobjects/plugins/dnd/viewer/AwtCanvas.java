package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.StringTokenizer;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.image.AwtImage;


public class AwtCanvas implements Canvas {
    private java.awt.Color color;
    private Font font;
    private final Graphics graphics;
    private final RenderingArea renderingArea;

    private AwtCanvas(final Graphics graphics, RenderingArea renderingArea) {
        this.graphics = graphics;
        this.renderingArea = renderingArea;
    }

    public AwtCanvas(final Graphics bufferGraphic, final RenderingArea renderingArea, final int x, final int y, final int width, final int height) {
        graphics = bufferGraphic;
        this.renderingArea = renderingArea;
        graphics.clipRect(x, y, width, height);
    }

    public void clearBackground(final View view, final Color color) {
        final Bounds bounds = view.getBounds();
        drawSolidRectangle(0, 0, bounds.getWidth(), bounds.getHeight(), color);
    }

    private Polygon createOval(final int x, final int y, final int width, final int height) {
        final int points = 40;
        final int xPoints[] = new int[points];
        final int yPoints[] = new int[points];
        double radians = 0.0;
        for (int i = 0; i < points; i++) {
            xPoints[i] = x + (int) (width / 2.0) + (int) (width / 2.0 * Math.cos(radians));
            yPoints[i] = y + (int) (height / 2.0) + (int) (height / 2.0 * Math.sin(radians));
            radians += (2.0 * Math.PI) / points;
        }
        final Polygon p = new Polygon(xPoints, yPoints, points);
        return p;
    }

    public Canvas createSubcanvas() {
        return new AwtCanvas(graphics.create(), renderingArea);
    }

    public Canvas createSubcanvas(final Bounds bounds) {
        return createSubcanvas(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public Canvas createSubcanvas(final int x, final int y, final int width, final int height) {
        final Graphics g = graphics.create();
        // this form of clipping must go here!
        g.translate(x, y);
        return new AwtCanvas(g, renderingArea, 0, 0, width, height);
    }

    public void draw3DRectangle(
            final int x,
            final int y,
            final int width,
            final int height,
            final Color color,
            final boolean raised) {
        useColor(color);
        graphics.draw3DRect(x, y, width - 1, height - 1, raised);
    }

    public void drawDebugOutline(final Bounds bounds, final int baseline, final Color color) {
        final int width = bounds.getWidth();
        final int height = bounds.getHeight();
        drawRectangle(bounds.getX(), bounds.getY(), width, height, color);
        final int midpoint = bounds.getY() + height / 2;
        drawLine(bounds.getX(), midpoint, width - 2, midpoint, color);
        if (baseline > 0) {
            drawLine(bounds.getX(), baseline, width - 1, baseline, Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BASELINE));
        }
    }

    public void drawImage(final Image image, final int x, final int y) {
        graphics.drawImage(((AwtImage) image).getAwtImage(), x, y, (ImageObserver) renderingArea);
    }

    public void drawImage(final Image image, final int x, final int y, final int width, final int height) {
        graphics.drawImage(((AwtImage) image).getAwtImage(), x, y, width - 1, height - 1, (ImageObserver) renderingArea);
    }

    public void drawLine(final int x, final int y, final int x2, final int y2, final Color color) {
        useColor(color);
        graphics.drawLine(x, y, x2, y2);
    }

    public void drawLine(final Location start, final int xExtent, final int yExtent, final Color color) {
        drawLine(start.getX(), start.getY(), start.getX() + xExtent, start.getY() + yExtent, color);
    }

    public void drawOval(final int x, final int y, final int width, final int height, final Color color) {
        useColor(color);
        final Polygon p = createOval(x, y, width - 1, height - 1);
        graphics.drawPolygon(p);
    }

    public void drawRectangle(final int x, final int y, final int width, final int height, final Color color) {
        useColor(color);
        graphics.drawRect(x, y, width - 1, height - 1);
    }

    public void drawRectangleAround(final View view, final Color color) {
        final Bounds bounds = view.getBounds();
        drawRectangle(0, 0, bounds.getWidth(), bounds.getHeight(), color);
    }

    public void drawRoundedRectangle(
            final int x,
            final int y,
            final int width,
            final int height,
            final int arcWidth,
            final int arcHeight,
            final Color color) {
        useColor(color);
        graphics.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);
    }

    public void drawShape(final Shape shape, final Color color) {
        useColor(color);
        graphics.drawPolygon(shape.getX(), shape.getY(), shape.count());
    }

    public void drawShape(final Shape shape, final int x, final int y, final Color color) {
        final Shape copy = new Shape(shape);
        copy.translate(x, y);
        drawShape(copy, color);
    }

    public void drawSolidOval(final int x, final int y, final int width, final int height, final Color color) {
        useColor(color);
        final Polygon p = createOval(x, y, width, height);
        graphics.fillPolygon(p);
    }

    public void drawSolidRectangle(final int x, final int y, final int width, final int height, final Color color) {
        useColor(color);
        graphics.fillRect(x, y, width, height);
    }

    public void drawSolidShape(final Shape shape, final Color color) {
        useColor(color);
        graphics.fillPolygon(shape.getX(), shape.getY(), shape.count());
    }

    public void drawSolidShape(final Shape shape, final int x, final int y, final Color color) {
        final Shape copy = new Shape(shape);
        copy.translate(x, y);
        drawSolidShape(copy, color);
    }

    public void drawText(final String text, final int x, final int y, final Color color, final Text style) {
        useColor(color);
        useFont(style);
        graphics.drawString(text, x, y);
    }

    public void drawText(final String text, final int x, final int y, final int maxWidth, final Color color, final Text style) {
        useColor(color);
        useFont(style);

        int top = y;
        final StringTokenizer lines = new StringTokenizer(text, "\n\r");
        while (lines.hasMoreTokens()) {
            final String line = lines.nextToken();
            final StringTokenizer words = new StringTokenizer(line, " ");
            final StringBuffer l = new StringBuffer();
            int width = 0;
            while (words.hasMoreTokens()) {
                final String nextWord = words.nextToken();
                final int wordWidth = style.stringWidth(nextWord);
                width += wordWidth;
                if (width >= maxWidth) {
                    graphics.drawString(l.toString(), x + (line.startsWith("\t") ? 20 : 00), top);
                    top += style.getLineHeight();
                    l.setLength(0);
                    width = wordWidth;
                }
                l.append(nextWord);
                l.append(" ");
                width += style.stringWidth(" ");
            }
            graphics.drawString(l.toString(), x + (line.startsWith("\t") ? 20 : 00), top);
            top += style.getLineHeight();
        }
    }

    public void offset(final int x, final int y) {
        graphics.translate(x, y);
    }

    public boolean overlaps(final Bounds bounds) {
        final Rectangle clip = graphics.getClipBounds();
        final Bounds activeArea = new Bounds(clip.x, clip.y, clip.width, clip.height);
        return bounds.intersects(activeArea);
    }

    @Override
    public String toString() {
        final Rectangle cb = graphics.getClipBounds();
        return "Canvas [area=" + cb.x + "," + cb.y + " " + cb.width + "x" + cb.height + ",color=" + color + ",font=" + font + "]";
    }

    private void useColor(final Color color) {
        final java.awt.Color awtColor = ((AwtColor) color).getAwtColor();

        if (this.color != awtColor) {
            this.color = awtColor;
            graphics.setColor(awtColor);
        }
    }

    private void useFont(final Text style) {
        final Font font = ((AwtText) style).getAwtFont();
        if (this.font != font) {
            this.font = font;
            graphics.setFont(font);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
