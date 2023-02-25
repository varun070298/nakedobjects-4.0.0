package org.nakedobjects.plugins.dnd.viewer.view.message;

import java.util.StringTokenizer;

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
import org.nakedobjects.plugins.dnd.viewer.action.CancelAction;
import org.nakedobjects.plugins.dnd.viewer.border.ButtonBorder;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugOutput;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class DetailedMessageViewSpecification implements ViewSpecification {

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content instanceof MessageContent && ((MessageContent) content).getDetail() != null;
    }

    public String getName() {
        return "Detailed Message";
    }

    public View createView(final Content content, final ViewAxis axis) {
        final ButtonAction actions[] = new ButtonAction[] { new AbstractButtonAction("Print...") {
            public void execute(final Workspace workspace, final View view, final Location at) {
                DebugOutput.print("Print exception", extract(view));
            }
        }, new AbstractButtonAction("Save...") {
            public void execute(final Workspace workspace, final View view, final Location at) {
                DebugOutput.saveToFile("Save exception", "Exception", extract(view));
            }
        }, new AbstractButtonAction("Copy") {
            public void execute(final Workspace workspace, final View view, final Location at) {
                DebugOutput.saveToClipboard(extract(view));
            }
        }, new CancelAction(),

        };
        
        DetailedMessageView messageView = new DetailedMessageView(content, this, null);
        return new ButtonBorder(actions, new ScrollBorder(messageView));
    }

    private String extract(final View view) {
        final Content content = view.getContent();
        final String message = ((MessageContent) content).getMessage();
        final String heading = ((MessageContent) content).title();
        final String detail = ((MessageContent) content).getDetail();

        final StringBuffer text = new StringBuffer();
        text.append(heading);
        text.append("\n\n");
        text.append(message);
        text.append("\n\n");
        text.append(detail);
        text.append("\n\n");
        return text.toString();
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

    public boolean isSubView() {
        return false;
    }
    
    public boolean isResizeable() {
        return true;
    }
}

class DetailedMessageView extends AbstractView {
    protected DetailedMessageView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public Size getMaximumSize() {
        final Size size = new Size();
        size.extendHeight(Toolkit.getText(ColorsAndFonts.TEXT_TITLE).getTextHeight());
        size.extendHeight(30);

        final String message = ((MessageContent) getContent()).getMessage();
        size.ensureWidth(500);
        size.extendHeight(Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).stringHeight(message, 500));
        size.extendHeight(30);

        final String detail = ((MessageContent) getContent()).getDetail();
        final StringTokenizer st = new StringTokenizer(detail, "\n\r");
        while (st.hasMoreTokens()) {
            final String line = st.nextToken();
            Text text = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
            size.ensureWidth((line.startsWith("\t") ? 20 : 0) + text.stringWidth(line));
            size.extendHeight(text.getTextHeight());
        }

        size.extend(40, 20);
        return size;
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final int left = 10;
        Text title = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
        int y = 10 + title.getAscent();
        final String message = ((MessageContent) getContent()).getMessage();
        final String heading = ((MessageContent) getContent()).title();
        final String detail = ((MessageContent) getContent()).getDetail();

        Color black = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        canvas.drawText(heading, left, y, black, title);
        y += title.getTextHeight();
        Text text = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        canvas.drawText(message, left, y, 500, black, text);

        y += text.stringHeight(message, 500);
        canvas.drawText(detail, left, y, 1000, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1), text);
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return ViewAreaType.VIEW;
    }

    @Override
    public void setFocusManager(final FocusManager focusManager) {
    }

}
// Copyright (c) Naked Objects Group Ltd.
