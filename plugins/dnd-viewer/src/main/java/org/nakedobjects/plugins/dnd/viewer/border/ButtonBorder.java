package org.nakedobjects.plugins.dnd.viewer.border;

import java.awt.event.KeyEvent;

import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractControlView;
import org.nakedobjects.plugins.dnd.viewer.action.Button;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class ButtonBorder extends AbstractBorder {
    private static final int BUTTON_SPACING = 5;
    private final AbstractControlView[] buttons;
    private ButtonAction defaultAction;

    public ButtonBorder(final ButtonAction[] actions, final View view) {
        super(view);

        buttons = new AbstractControlView[actions.length];
        for (int i = 0; i < actions.length; i++) {
            final ButtonAction action = actions[i];
            buttons[i] = new Button(action, view);
            if (action.isDefault()) {
                defaultAction = action;
            }
        }
        // space for: line & button with whitespace
        bottom = 1 + VPADDING + buttons[0].getRequiredSize(new Size()).getHeight() + VPADDING;

    }

    @Override
    public void draw(final Canvas canvas) {
        // draw buttons
        for (int i = 0; i < buttons.length; i++) {
            final Canvas buttonCanvas = canvas.createSubcanvas(buttons[i].getBounds());
            buttons[i].draw(buttonCanvas);
            final int buttonWidth = buttons[i].getSize().getWidth();
            buttonCanvas.offset(BUTTON_SPACING + buttonWidth, 0);
        }

        // draw rest
        super.draw(canvas);
    }

    @Override
    public void firstClick(final Click click) {
        final View button = overButton(click.getLocation());
        if (button == null) {
            super.firstClick(click);
        } else {
            button.firstClick(click);
        }
    }

    public AbstractControlView[] getButtons() {
        return buttons;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        size.ensureWidth(totalButtonWidth());
        size.extendWidth(BUTTON_SPACING * 2);
        return size;
    }

    @Override
    public View identify(final Location location) {
        for (int i = 0; i < buttons.length; i++) {
            final AbstractControlView button = buttons[i];
            if (button.getBounds().contains(location)) {
                return button;
            }
        }
        return super.identify(location);
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (defaultAction != null && defaultAction.disabled(getView()).isAllowed()) {
                key.consume();
                defaultAction.execute(getWorkspace(), getView(), getLocation());
            }
        }

        super.keyPressed(key);
    }

    public void layout(final int width) {
        int x = width / 2 - totalButtonWidth() / 2;
        final int y = getSize().getHeight() - VPADDING - buttons[0].getRequiredSize(new Size()).getHeight();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = buttons[i];
            buttons[i].setSize(buttons[i].getRequiredSize(new Size()));
            buttons[i].setLocation(new Location(x, y));

            x += buttons[i].getSize().getWidth();
            x += BUTTON_SPACING;
        }
    }

    @Override
    public void mouseDown(final Click click) {
        final View button = overButton(click.getLocation());
        if (button == null) {
            super.mouseDown(click);
        } else {
            button.mouseDown(click);
        }
    }

    @Override
    public void mouseUp(final Click click) {
        final View button = overButton(click.getLocation());
        if (button == null) {
            super.mouseUp(click);
        } else {
            button.mouseUp(click);
        }
    }

    /**
     * Finds the action button under the pointer; returning null if none.
     */
    private View overButton(final Location location) {
        for (int i = 0; i < buttons.length; i++) {
            final AbstractControlView button = buttons[i];
            if (button.getBounds().contains(location)) {
                return button;
            }
        }
        return null;
    }

    @Override
    public void secondClick(final Click click) {
        final View button = overButton(click.getLocation());
        if (button == null) {
            super.secondClick(click);
        }
    }

    @Override
    public void setBounds(final Bounds bounds) {
        super.setBounds(bounds);
        layout(bounds.getWidth());
    }

    @Override
    public void setSize(final Size size) {
        super.setSize(size);
        layout(size.getWidth());
    }

    @Override
    public void thirdClick(final Click click) {
        final View button = overButton(click.getLocation());
        if (button == null) {
            super.thirdClick(click);
        }
    }

    private int totalButtonWidth() {
        int totalButtonWidth = 0;
        for (int i = 0; i < buttons.length; i++) {
            final int buttonWidth = buttons[i].getRequiredSize(new Size()).getWidth();
            totalButtonWidth += i > 0 ? BUTTON_SPACING : 0;
            totalButtonWidth += buttonWidth;
        }
        return totalButtonWidth;
    }

}
// Copyright (c) Naked Objects Group Ltd.
