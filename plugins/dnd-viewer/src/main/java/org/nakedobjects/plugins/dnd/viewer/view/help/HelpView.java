package org.nakedobjects.plugins.dnd.viewer.view.help;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;


public class HelpView extends AbstractView implements TextBlockTarget {
    private static final int HEIGHT = 350;
    private static final int WIDTH = 400;
    private static final int MAX_TEXT_WIDTH = 375;
    private final TextContent content;

    public HelpView(final String name, final String description, final String help) {
        super(new NullContent());
        final String text = (name == null || name.trim().equals("") ? "" : (name + "\n"))
                + (description == null || description.trim().equals("") ? "" : (description + "\n")) + (help == null ? "" : help);
        content = new TextContent(this, 10, TextContent.WRAPPING);
        content.setText(text);
    }

    @Override
    public void draw(final Canvas canvas) {
        int x = 0;
        int y = 0;
        final int xEntent = getSize().getWidth() - 1;
        final int yExtent = getSize().getHeight() - 1;

        final int arc = 9;
        canvas.drawSolidRectangle(x + 2, y + 2, xEntent - 4, yExtent - 4, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        Color black = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        canvas.drawRoundedRectangle(x, y++, xEntent, yExtent, arc, arc, black);
        canvas.drawRoundedRectangle(x + 1, y++, xEntent - 2, yExtent - 2, arc, arc, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        canvas.drawRoundedRectangle(x + 2, y++, xEntent - 4, yExtent - 4, arc, arc, black);

        x += 10;
        y += VPADDING;
        y += Toolkit.getText(ColorsAndFonts.TEXT_TITLE).getTextHeight();
        canvas.drawText("Help", x, y, black, Toolkit.getText(ColorsAndFonts.TEXT_TITLE));

        final String[] lines = content.getDisplayLines();
        for (int i = 0; i < lines.length; i++) {
            y += Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getLineHeight();
            canvas.drawText(lines[i], x, y, MAX_TEXT_WIDTH, black, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
        }
    }

    @Override
    public Size getMaximumSize() {
        return new Size(WIDTH, HEIGHT);
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final int height = Math.min(HEIGHT, maximumSize.getHeight());
        final int width = Math.min(WIDTH, maximumSize.getWidth());
        return new Size(width, height);
    }

    /**
     * Removes the help view when clicked on.
     */
    @Override
    public void firstClick(final Click click) {
        getViewManager().clearOverlayView(this);
    }

    public int getMaxFieldWidth() {
        return WIDTH - 20;
    }

    public Text getText() {
        return Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    }
}
// Copyright (c) Naked Objects Group Ltd.
