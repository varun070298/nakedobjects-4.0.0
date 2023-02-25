package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class PasswordField extends TextField {
    protected static final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    private int maxTextWidth;
    private char echoCharacter;

    public PasswordField(final Content content, final ViewSpecification design, final ViewAxis axis) {
        super((TextParseableContent) content, design, axis, true, TextContent.NO_WRAPPING);
        setMaxTextWidth(TEXT_WIDTH);
        final String echoCharacterSetting = NakedObjectsContext.getConfiguration().getString(Properties.PROPERTY_BASE + "echo");
        if (echoCharacterSetting == null || echoCharacterSetting.equals(" ")) {
            echoCharacter = '*';
        } else {
            echoCharacter = echoCharacterSetting.charAt(0);
        }
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        options.add(new ClearValueOption(this));
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_VALUE));
    }

    protected boolean provideClearCopyPaste() {
        return false;
    }

    /**
     * Only allow deletion of last character, ie don;t allow editing of the internals of the password.
     */
    public void delete() {
        textContent.deleteLeft(cursor);
        cursor.left();
        selection.resetTo(cursor);
        changeMade();
    }

    /**
     * disable left key.
     */
    protected void left(boolean alt, boolean shift) {}

    /**
     * disable right key.
     */
    protected void right(boolean alt, boolean shift) {}

    /**
     * disable home key.
     */
    protected void home(boolean alt, boolean shift) {}

    /**
     * disable end key.
     */
    protected void end(boolean alt, boolean shift) {}

    /**
     * disable page down key.
     */
    protected void pageDown(boolean shift, boolean ctrl) {}

    /**
     * disable page up key.
     */
    protected void pageUp(boolean shift, boolean ctrl) {}

    private String echoPassword(String password) {
        final int length = password.length();
        String echoedPassword = "";
        for (int i = 0; i < length; i++) {
            echoedPassword += echoCharacter;
        }
        return echoedPassword;
    }

    @Override
    public Size getMaximumSize() {
        final int width = HPADDING + maxTextWidth + HPADDING;
        int height = style.getTextHeight() + VPADDING;
        height = Math.max(height, Toolkit.defaultFieldHeight());

        return new Size(width, height);
    }

    /**
     * Set the maximum width of the field, as a number of characters
     */
    private void setMaxTextWidth(final int noCharacters) {
        maxTextWidth = style.charWidth('o') * noCharacters;
    }

    protected void align() {}

    protected void drawHighlight(Canvas canvas, int maxWidth) {}

    @Override
    protected void drawLines(final Canvas canvas, final Color color, final int width) {
        final int baseline = getBaseline();
        canvas.drawLine(HPADDING, baseline, HPADDING + width, baseline, color);
    }

    @Override
    protected void drawText(final Canvas canvas, final Color textColor, final int width) {

        final String[] lines = textContent.getDisplayLines();
        if (lines.length > 1) {
            throw new NakedObjectException("Password field should contain a string that contains no line breaks; contains "
                    + lines.length);
        }

        final String chars = lines[0];
        if (chars == null) {
            throw new NakedObjectException();
        }
        if (chars.endsWith("\n")) {
            throw new NakedObjectException();
        }

        final int baseline = getBaseline();
        String echoPassword = echoPassword(chars);

        // draw cursor
        if (hasFocus() && canChangeValue().isAllowed()) {
            final int pos = style.stringWidth(echoPassword) - HPADDING;
            Color color = Toolkit.getColor(ColorsAndFonts.COLOR_TEXT_CURSOR);
            canvas.drawLine(pos, (baseline + style.getDescent()), pos, baseline - style.getAscent(), color);
        }

        // draw text
        canvas.drawText(echoPassword, HPADDING, baseline, textColor, style);
    }

}
// Copyright (c) Naked Objects Group Ltd.
