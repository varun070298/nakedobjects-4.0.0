package org.nakedobjects.plugins.dnd.viewer.view.message;

import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractButtonAction;
import org.nakedobjects.plugins.dnd.viewer.border.ButtonBorder;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.focus.SubviewFocusManager;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class MessageDialogSpecification implements ViewSpecification {

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content instanceof MessageContent;
    }

    public String getName() {
        return "Message Dialog";
    }

    public View createView(final Content content, final ViewAxis axis) {
        final ButtonAction actions[] = new ButtonAction[] { new CloseViewAction() };
        MessageView messageView = new MessageView((MessageContent) content, this, null);
        final View dialogView = new ButtonBorder(actions, new ScrollBorder(messageView));
        dialogView.setFocusManager(new SubviewFocusManager(dialogView));
        return dialogView;
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }
    
    public boolean isResizeable() {
        return true;
    }

    public boolean isSubView() {
        return false;
    }

    public static class CloseViewAction extends AbstractButtonAction {
        public CloseViewAction() {
            super("Close");
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            view.dispose();
        }
    }
}

class MessageView extends AbstractView {
    private static final int MAX_TEXT_WIDTH = 400;
    private static final int LEFT = 20;
    private static final int RIGHT = 20;
    private static final int TOP = 15;
    private static final int PADDING = 10;
    private Image errorIcon;
    private FocusManager focusManager;

    protected MessageView(final MessageContent content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        final String iconName = ((MessageContent) getContent()).getIconName();
        errorIcon = ImageFactory.getInstance().loadIcon(iconName, 32, null);
        if (errorIcon == null) {
            errorIcon = ImageFactory.getInstance().loadDefaultIcon(32, null);
        }
    }

    @Override
    public Size getMaximumSize() {
        final Size size = new Size();

        final String message = ((MessageContent) getContent()).getMessage();
        final String heading = ((MessageContent) getContent()).title();

        size.ensureHeight(errorIcon.getHeight());
        Text text = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        Text titleText = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
        size.extendWidth(text.stringWidth(message, MAX_TEXT_WIDTH));
        int textHeight = titleText.getLineHeight();
        textHeight += text.stringHeight(message, MAX_TEXT_WIDTH);
        size.ensureHeight(textHeight);

        size.ensureWidth(titleText.stringWidth(heading));

        size.extendWidth(errorIcon.getWidth());
        size.extendWidth(PADDING);

        size.extend(LEFT + RIGHT, TOP * 2);
        return size;
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final String message = ((MessageContent) getContent()).getMessage();
        final String heading = ((MessageContent) getContent()).title();

        canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));

        canvas.drawImage(errorIcon, LEFT, TOP);

        final int x = LEFT + errorIcon.getWidth() + PADDING;
        int y = TOP + 3 + Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent();
        Color black = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        if (!heading.equals("")) {
            Text title = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
            canvas.drawText(heading, x, y, black, title);
            y += title.getLineHeight();
        }
        canvas.drawText(message, x, y, MAX_TEXT_WIDTH, black, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return ViewAreaType.VIEW;
    }

    @Override
    public FocusManager getFocusManager() {
        return focusManager == null ? super.getFocusManager() : focusManager;
    }

    @Override
    public void setFocusManager(final FocusManager focusManager) {
        this.focusManager = focusManager;
    }

}
// Copyright (c) Naked Objects Group Ltd.
