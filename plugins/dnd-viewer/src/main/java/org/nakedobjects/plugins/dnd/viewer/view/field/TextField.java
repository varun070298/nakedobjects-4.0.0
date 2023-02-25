package org.nakedobjects.plugins.dnd.viewer.view.field;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.LabelAxis;
import org.nakedobjects.plugins.dnd.SimpleInternalDrag;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.border.BackgroundBorder;
import org.nakedobjects.plugins.dnd.viewer.border.LineBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.simple.TextView;
import org.nakedobjects.plugins.dnd.viewer.view.text.CursorPosition;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextSelection;
import org.nakedobjects.runtime.persistence.ConcurrencyException;


public abstract class TextField extends TextParseableFieldAbstract implements TextBlockTarget {
    private static final Logger LOG = Logger.getLogger(TextField.class);
    protected static final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    protected CursorPosition cursor;
    private boolean identified;
    private String invalidReason = null;
    private boolean isSaved = true;
    private final int maxLength;
    private int displayWidth;
    protected TextSelection selection;
    private final boolean showLines;
    protected TextContent textContent;

    public TextField(
            final TextParseableContent content,
            final ViewSpecification specification,
            final ViewAxis axis,
            final boolean showLines,
            final int wrapStyle) {
        super(content, specification, axis);
        this.showLines = showLines;

        int typicalLength = content.getTypicalLineLength();
        typicalLength = typicalLength == 0 ? TEXT_WIDTH : typicalLength / content.getNoLines();
        setTextWidth(typicalLength);

        this.maxLength = content.getMaximumLength();

        textContent = new TextContent(this, 1, wrapStyle);
        cursor = new CursorPosition(textContent, 0, 0);
        selection = new TextSelection(textContent);
        final NakedObject value = getValue();
        textContent.setText(value == null ? "" : titleString(value));
        cursor.home();
        isSaved = true;
    }

    protected abstract void align();

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);
        options.add(new RevertFieldOption(this));
    }

    @Override
    protected void clear() {
        clearValue();
        editComplete(false, false);
    }

    void clearValue() {
        textContent.setText("");
        cursor.home();
        selection.resetTo(cursor);
        changeMade();
    }

    @Override
    protected void copyToClipboard() {
        final boolean noSelection = selection.to().samePosition(selection.from());
        final String text = noSelection ? textContent.getText() : textContent.getText(selection);
        getViewManager().setClipboard(text, String.class);
        LOG.debug("copied " + text);
    }

    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        debug.appendln("text", textContent);
    }

    /**
     * Delete the character to the left of the cursor.
     */
    public void delete() {
        if (selection.hasSelection()) {
            textContent.delete(selection);
            selection.resetTo(selection.from());
        } else {
            textContent.deleteLeft(cursor);
            cursor.left();
            selection.resetTo(cursor);
        }
        changeMade();
    }

    /**
     * Delete the character to the right of the cursor.
     */
    public void deleteForward() {
        if (selection.hasSelection()) {
            textContent.delete(selection);
            selection.resetTo(selection.from());
        } else {
            textContent.deleteRight(cursor);
        }
        changeMade();
    }

    protected void down(final boolean shift) {
        cursor.lineDown();
        highlight(shift);
        markDamaged();
    }

    @Override
    public void drag(final InternalDrag drag) {
        if (canChangeValue().isAllowed()) {
            selection.extendTo(drag.getLocation());
            markDamaged();
        }
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        final Location at = drag.getLocation();

        final Size size = getView().getSize();
        final Location anchor = getView().getAbsoluteLocation();
        final ViewAxis axis = getViewAxis();
        if (axis instanceof LabelAxis) {
            final int width = ((LabelAxis) axis).getWidth();
            size.contractWidth(width);
            anchor.add(width, 0);
        }

        if (canChangeValue().isAllowed()) {
            cursor.cursorAt(at);
            resetSelection();
            return new SimpleInternalDrag(this, anchor);
        }

        markDamaged();

        return null;
    }

    @Override
    public void dragTo(final InternalDrag drag) {
        final Location at = drag.getLocation();
        if (canChangeValue().isAllowed()) {
            selection.extendTo(at);
            markDamaged();
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final int width = getMaxFieldWidth();

        align();

        if (hasFocus() && selection.hasSelection()) {
            drawHighlight(canvas, width);
        }

        /*
        if (showLines == true && canChangeValue().isAllowed()) {
            Color color = identified ? Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
            color = hasFocus() ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1) : color;
            drawLines(canvas, color, width);
        }
*/
        Color textColor;
        Color lineColor;
        if (getState().isInvalid()) {
            textColor = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
            lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
        } else if (hasFocus()) {
            if (isSaved) {
                textColor = Toolkit.getColor(ColorsAndFonts.COLOR_TEXT_SAVED);
                lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
            } else {
                textColor = Toolkit.getColor(ColorsAndFonts.COLOR_TEXT_EDIT);
                lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
            }
        } else if (identified) {
            textColor = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
            lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);            
        } else {
            textColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
            lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
        }
        
        if (showLines == true && canChangeValue().isAllowed()) {
            drawLines(canvas, lineColor, width);
        }
        drawText(canvas, textColor, width);
    }

    protected abstract void drawHighlight(final Canvas canvas, final int maxWidth);

    protected abstract void drawLines(final Canvas canvas, final Color color, final int width);

    protected abstract void drawText(final Canvas canvas, final Color textColor, final int width);

    @Override
    public void editComplete(final boolean moveFocus, boolean toNextField) {
        if (canChangeValue().isAllowed() && !isSaved) {
            isSaved = true;
            initiateSave(moveFocus);
        } else if (moveFocus) {
            if (toNextField) {
                getFocusManager().focusNextView();
            } else {                
                getFocusManager().focusPreviousView();
            }
        }
    }

    protected void end(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.bottom();
        } else {
            cursor.end();
        }

        highlight(shift);
        markDamaged();
    }

    /**
     * Called when 'enter' has been pressed. Return indicates whether event has been consumed; by default it
     * hasn't. This default implementation marks field as having edit completed.
     */
    protected boolean enter() {
        editComplete(false, false);
        return false;
    }

    @Override
    public void entered() {
        if (canChangeValue().isAllowed()) {
            getFeedbackManager().showTextCursor();
            identified = true;
            markDamaged();
        }
        super.entered();
    }

    protected void escape() {
        if (isSaved) {
            clearValue();
        } else {
            invalidReason = null;
            refresh();
            markDamaged();
        }
    }

    @Override
    public void exited() {
        if (canChangeValue().isAllowed()) {
            getFeedbackManager().showDefaultCursor();
            identified = false;
            markDamaged();
        }
        super.exited();
    }

    /**
     * Responds to first click by placing the cursor between the two characters nearest the point of the
     * mouse.
     */
    @Override
    public void firstClick(final Click click) {
        if (canChangeValue().isAllowed()) {
            final Location at = click.getLocation();
            at.subtract(HPADDING, VPADDING);
            cursor.cursorAt(at);
            resetSelection();

            // testing
            if (cursor.getLine() > textContent.getNoLinesOfContent()) {
                throw new NakedObjectException("not inside content for line " + cursor.getLine() + " : "
                        + textContent.getNoLinesOfContent());
            }

            markDamaged();
        }

        if (!canChangeValue().isAllowed() || click.isShift() || click.button2()) {
            NakedObject valueAdapter = getContent().getNaked();
            if (valueAdapter != null && valueAdapter.titleString().length() > 0) {
                final View textView = new BackgroundBorder(Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3), 
                        new LineBorder(1, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1), 
                        new TextView(getContent(), null, null)));
                getViewManager().setOverlayView(textView);
                
                int offset = getView().getPadding().getLeft();
                Location location = getAbsoluteLocation();
                location.add(offset, 0);
                textView.setLocation(location);
                textView.markDamaged();
            }
        }
    }

    @Override
    public void focusLost() {
        super.focusLost();
        editComplete(false, false);
    }

    @Override
    public void focusReceived() {
        getFeedbackManager().setError(invalidReason == null ? "" : invalidReason);
        resetSelection();
    }

    @Override
    public int getBaseline() {
        return getText().getAscent();
    }

    @Override
    public Size getMaximumSize() {
        final int width = HPADDING + displayWidth + HPADDING;
        int height;
        if (textContent.getNoDisplayLines() == 1) {
            height = getText().getTextHeight();
        } else {
            height = textContent.getNoDisplayLines() * getText().getLineHeight();
        }
        height = Math.max(height, Toolkit.defaultFieldHeight());
        return new Size(width, height);
    }

    public int getMaxFieldWidth() {
        return displayWidth;
    }

    public Text getText() {
        return style;
    }

    @Override
    String getSelectedText() {
        return textContent.getText(selection);
    }

    private NakedObject getValue() {
        return getContent().getNaked();
    }

    /**
     * modifies the selection object so that text is selected if the flag is true, or text is unselected if
     * false.
     */
    private void highlight(final boolean select) {
        if (canChangeValue().isAllowed()) {
            if (!select) {
                selection.resetTo(cursor);
            } else {
                selection.extendTo(cursor);
            }
        }
    }

    protected void home(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.top();
        } else {
            cursor.home();
        }

        highlight(shift);
        markDamaged();
    }

    private void insert(final char character) {
        if (withinMaximum(1)) {
            insert("" + character);
            selection.resetTo(cursor);
        } else {
            getFeedbackManager().setError("Entry can be no longer than " + maxLength + " characters");
        }
    }

    protected void changeMade() {
        isSaved = false;
        markDamaged();
        if (getState().isInvalid()) {
            getState().clearInvalid();
            getFeedbackManager().clearError();
        }
    }

    private void insert(final String characters) {
        if (withinMaximum(characters.length())) {
            final int noLines = textContent.getNoDisplayLines();
            textContent.insert(cursor, characters);
            cursor.right(characters.length());
            if (textContent.getNoDisplayLines() != noLines) {
                invalidateLayout();
            }
            changeMade();
        } else {
            getFeedbackManager().setError("Entry can be no longer than " + maxLength + " characters");
        }
    }

    public boolean isIdentified() {
        return identified;
    }

    /**
     * Called when the user presses any key on the keyboard while this view has the focus.
     */
    @Override
    public void keyPressed(final KeyboardAction key) {
        if (!canChangeValue().isAllowed()) {
            return;
        }

        final int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT) {
            return;
        }

        final int modifiers = key.getModifiers();
        // modifiers
        final boolean alt = (modifiers & InputEvent.ALT_MASK) > 0;
        final boolean shift = (modifiers & InputEvent.SHIFT_MASK) > 0;
        final boolean ctrl = (modifiers & InputEvent.CTRL_MASK) > 0;

        switch (keyCode) {
        case KeyEvent.VK_PAGE_UP:
            key.consume();
            pageUp(shift, ctrl);
            break;
        case KeyEvent.VK_PAGE_DOWN:
            key.consume();
            pageDown(shift, ctrl);
            break;
        case KeyEvent.VK_V:
            if (ctrl) {
                key.consume();
                pasteFromClipboard();
                highlight(false);
            }
            break;
        case KeyEvent.VK_C:
            if (ctrl) {
                key.consume();
                copyToClipboard();
            }
            break;
        case KeyEvent.VK_DOWN:
            key.consume();
            down(shift);
            break;
        case KeyEvent.VK_UP:
            key.consume();
            up(shift);
            break;
        case KeyEvent.VK_HOME:
            key.consume();
            home(alt, shift);
            break;
        case KeyEvent.VK_END:
            key.consume();
            end(alt, shift);
            break;
        case KeyEvent.VK_LEFT:
            key.consume();
            left(alt, shift);
            break;
        case KeyEvent.VK_RIGHT:
            key.consume();
            right(alt, shift);
            break;
        case KeyEvent.VK_DELETE:
            key.consume();
            deleteForward();
            break;
        case KeyEvent.VK_BACK_SPACE:
            key.consume();
            delete();
            break;
        case KeyEvent.VK_TAB:
            key.consume();
            boolean moveToNextField = !shift;
            tab(moveToNextField);
            break;
        case KeyEvent.VK_ENTER:
            if (!enter()) {
                getParent().keyPressed(key);
            }
            break;
        case KeyEvent.VK_ESCAPE:
            key.consume();
            escape();
            break;

        default:
            break;
        }

        LOG.debug("character at " + cursor.getCharacter() + " line " + cursor.getLine());
        LOG.debug(selection);
    }

    /**
     * Called when the user releases any key on the keyboard while this view has the focus.
     */
    @Override
    public void keyReleased(final int keyCode, final int modifiers) {}

    /**
     * Called when the user presses a non-control key (i.e. data entry keys and not shift, up-arrow etc). Such
     * a key press will result in a prior call to <code>keyPressed</code> and a subsequent call to
     * <code>keyReleased</code>.
     */
    @Override
    public void keyTyped(final char keyCode) {
        if (canChangeValue().isAllowed()) {
            insert(keyCode);
        }
    }

    protected void left(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.wordLeft();
        } else {
            cursor.left();
        }

        highlight(shift);
        markDamaged();
    }

    protected void pageDown(final boolean shift, final boolean ctrl) {
        if (ctrl) {
            if (textContent.decreaseDepth()) {
                textContent.alignDisplay(cursor.getLine());
                invalidateLayout();
            }
        } else {
            cursor.pageDown();
            highlight(shift);
        }
        markDamaged();
    }

    protected void pageUp(final boolean shift, final boolean ctrl) {
        if (ctrl) {
            textContent.increaseDepth();
            textContent.alignDisplay(cursor.getLine());
            invalidateLayout();
        } else {
            cursor.pageUp();
            highlight(shift);
        }
        markDamaged();
    }

    @Override
    protected void pasteFromClipboard() {
        try {
            final String text = (String) getViewManager().getClipboard(String.class);
            insert(text);
            LOG.debug("pasted " + text);
        } catch (final Throwable e) {
            LOG.error("invalid paste operation " + e);
        }
    }

    private String titleString(final NakedObject object) {
        return ((TextParseableContent) getContent()).titleString(object);
    }

    @Override
    public void refresh() {
        super.refresh();
        final NakedObject object = getValue();
        if (object == null) {
            textContent.setText("");
        } else {
            textContent.setText(titleString(object));
        }
        isSaved = true;
    }

    private void resetSelection() {
        selection.resetTo(cursor);
    }

    protected void right(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.wordRight();
        } else {
            cursor.right();
        }

        highlight(shift);
        markDamaged();
    }

    @Override
    protected void save() {
        final String entry = textContent.getText();

        // do nothing if entry is same as the value object
        final NakedObject value = getValue();
        if (!entry.equals(value == null ? "" : value.titleString())) {
            LOG.debug("field edited: \'" + entry + "\' to replace \'" + (value == null ? "" : value.titleString()) + "\'");

            try {
                parseEntry(entry.toString());
                invalidReason = null;
                getViewManager().getSpy().addAction("VALID ENTRY: " + entry);
                markDamaged();
                getParent().invalidateContent();
            } catch (final TextEntryParseException e) {
                invalidReason = "Invalid Entry: " + e.getMessage();
                getFeedbackManager().setError(invalidReason);
                getState().setInvalid();
                markDamaged();
            } catch (final InvalidEntryException e) {
                invalidReason = "Invalid Entry: " + e.getMessage();
                getFeedbackManager().setError(invalidReason);
                getState().setInvalid();
                markDamaged();
            } catch (final ConcurrencyException e) {
                invalidReason = "Update Failure: " + e.getMessage();
                getState().setOutOfSynch();
                markDamaged();
                throw e;
            } catch (final NakedObjectException e) {
                invalidReason = "Update Failure: " + e.getMessage();
                getFeedbackManager().setError(invalidReason);
                getState().setOutOfSynch();
                markDamaged();
                throw e;
            }
        }
    }

    @Override
    public void secondClick(final Click click) {
        if (canChangeValue().isAllowed()) {
            selection.selectWord();
        }
    }

    /**
     * Set the maximum width of the field, as a number of characters
     */
    private void setTextWidth(final int noCharacters) {
        displayWidth = getText().charWidth('5') * (noCharacters + 3);
    }

    /**
     * Set the width of the field, as a number of pixels
     */
    public void setWidth(final int width) {
        displayWidth = width;
    }

    @Override
    public void setSize(final Size size) {
        super.setSize(size);
        setWidth(size.getWidth() - 2 * HPADDING);
    }

    protected void tab(final boolean moveToNextField) {
        editComplete(true, moveToNextField);
    }

    @Override
    public void thirdClick(final Click click) {
        if (canChangeValue().isAllowed()) {
            selection.selectSentence();
            markDamaged();
        }
    }

    protected void up(final boolean shift) {
        cursor.lineUp();
        highlight(shift);
        markDamaged();
    }

    private boolean withinMaximum(final int characters) {
        return maxLength == 0 || textContent.getText().length() + characters <= maxLength;
    }

    void revertInvalidEntry() {
        invalidReason = null;
        refresh();
        cursor.home();
        getState().clearInvalid();
        getFeedbackManager().clearError();
        markDamaged();
    }

    public boolean hasInvalidEntry() {
        return invalidReason != null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
