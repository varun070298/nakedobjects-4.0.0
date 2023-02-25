package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.BackgroundTask;
import org.nakedobjects.plugins.dnd.BackgroundWork;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public abstract class AbstractField extends AbstractView {
    protected static final int TEXT_WIDTH = 20;
    private boolean identified;

    protected AbstractField(final Content content, final ViewSpecification design, final ViewAxis axis) {
        super(content, design, axis);
    }

    @Override
    public boolean canFocus() {
        return canChangeValue().isAllowed();
    }

    protected boolean provideClearCopyPaste() {
        return false;
    }
    
    protected boolean cantClear() {
        return true;
    }

    protected void clear() {}

    protected void copyToClipboard() {}

    protected void pasteFromClipboard() {}

    /**
     * Indicates the drag started within this view's bounds is continuing. By default does nothing.
     */
    @Override
    public void drag(final InternalDrag drag) {}

    /**
     * Default implementation - does nothing
     */
    @Override
    public void dragCancel(final InternalDrag drag) {}

    /**
     * Indicates the start of a drag within this view's bounds. By default does nothing.
     */
    @Override
    public View dragFrom(final Location location) {
        return null;
    }

    /**
     * Indicates the drag started within this view's bounds has been finished (although the location may now
     * be outside of its bounds). By default does nothing.
     */
    @Override
    public void dragTo(final InternalDrag drag) {}

    @Override
    public void draw(final Canvas canvas) {
        if (getState().isActive()) {
            canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED));
        }

        if (getState().isOutOfSynch()) {
            canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_OUT_OF_SYNC));
        }

        if (getState().isInvalid()) {
            Image image = ImageFactory.getInstance().loadIcon("invalid-entry", 12, null);
            if (image != null) {
                canvas.drawImage(image, getSize().getWidth() - 16, 2);
            }
            
            //canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_INVALID));
        }

        super.draw(canvas);
    }

    @Override
    public void entered() {
        super.entered();
        identified = true;
        final Consent changable = canChangeValue();
        if (changable.isVetoed()) {
            getFeedbackManager().setViewDetail(changable.getReason());
        }
        markDamaged();
    }

    @Override
    public void exited() {
        super.exited();
        identified = false;
        markDamaged();
    }

    public boolean getIdentified() {
        return identified;
    }

    @Override
    public Padding getPadding() {
        return new Padding(0, 0, 0, 0);
    }

    public View getRoot() {
        throw new NotYetImplementedException();
    }

    String getSelectedText() {
        return "";
    }

    @Override
    public boolean hasFocus() {
        return getViewManager().hasFocus(getView());
    }

    public boolean isEmpty() {
        return false;
    }
    
    public boolean indicatesForView(final Location mouseLocation) {
        return false;
    }

    /**
     * Called when the user presses any key on the keyboard while this view has the focus.
     */
    @Override
    public void keyPressed(final KeyboardAction key) {}

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
    public void keyTyped(final char keyCode) {}

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        if (provideClearCopyPaste()) {
            options.add(new CopyValueOption(this));
            options.add(new PasteValueOption(this));
            options.add(new ClearValueOption(this));
        }
        
        if (getView().getSpecification().isReplaceable()) {
            replaceOptions(Toolkit.getViewFactory().valueViews(getContent(), this), options);
        }

        super.contentMenuOptions((options));
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_VALUE));
    }

    protected final void initiateSave(final boolean moveToNextField) {
        BackgroundWork.runTaskInBackground(this, new BackgroundTask() {
            public void execute() {
                save();
                getParent().updateView();
                invalidateLayout();
                if (moveToNextField) {
                    getFocusManager().focusNextView();
                }
            }

            public String getName() {
                return "Save field";
            }

            public String getDescription() {
                return "Saving " + getContent().windowTitle();
            }

        });
    }

    protected abstract void save();

    @Override
    public String toString() {
        final ToString str = new ToString(this, getId());
        str.append("location", getLocation());
        final NakedObject nakedObject = getContent().getNaked();
        str.append("object", nakedObject == null ? "" : nakedObject.getObject());
        return str.toString();
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return ViewAreaType.INTERNAL;
    }

    @Override
    public int getBaseline() {
        return Toolkit.defaultBaseline();
    }
}
// Copyright (c) Naked Objects Group Ltd.
