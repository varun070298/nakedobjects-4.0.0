package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


class ColorFieldOverlay extends AbstractView {
    private static final int colors[] = new int[] { 0xffffff, 0x0, 0x666666, 0xcccccc, // white, black, dark
            // gray, light gray
            0x000099, 0x0066cc, 0x0033ff, 0x99ccff, // blues
            0x990000, 0xff0033, 0xcc0066, 0xff66ff, // reds
            0x003300, 0x00ff33, 0x669933, 0xccff66 // greens
    };
    private static final int COLUMNS = 4;
    private static final int ROWS = 4;
    private static final int ROW_HEIGHT = 18;
    private static final int COLUMN_WIDTH = 23;

    private final ColorField field;

    public ColorFieldOverlay(final ColorField field) {
        super(field.getContent(), null, null);

        this.field = field;
    }

    @Override
    public Size getMaximumSize() {
        return new Size(COLUMNS * COLUMN_WIDTH, ROWS * ROW_HEIGHT);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawSolidRectangle(0, 0, COLUMNS * COLUMN_WIDTH - 1, ROWS * ROW_HEIGHT - 1, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
        for (int i = 0; i < colors.length; i++) {
            final Color color = Toolkit.getColor(colors[i]);
            final int y = i / COLUMNS * ROW_HEIGHT;
            final int x = i % COLUMNS * COLUMN_WIDTH;
            canvas.drawSolidRectangle(x, y, COLUMN_WIDTH - 1, ROW_HEIGHT - 1, color);
        }
        canvas.drawRectangle(0, 0, COLUMNS * COLUMN_WIDTH - 1, ROWS * ROW_HEIGHT - 1, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2));
    }

    @Override
    public void firstClick(final Click click) {
        final int x = click.getLocation().getX();
        final int y = click.getLocation().getY();
        final int color = colors[y / ROW_HEIGHT * COLUMNS + x / COLUMN_WIDTH];
        field.setColor(color);
        dispose();
    }
}
// Copyright (c) Naked Objects Group Ltd.
