package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.ObjectParameter;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.border.ObjectBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.lookup.OpenObjectDropDownBorder;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;


public class EmptyField extends AbstractView {

    public static class Specification implements ViewSpecification {
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return content != null && content.isObject() && requirement.is(ViewRequirement.OPEN) && !content.isTextParseable()
                    && content.getNaked() == null;
        }

        public View createView(final Content content, final ViewAxis axis) {
            final EmptyField emptyField = new EmptyField(content, this, axis, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
            if ((content instanceof OneToOneField && ((OneToOneField) content).isEditable().isAllowed())
                    || content instanceof ObjectParameter) {
                if (content.isOptionEnabled()) {
                    return new ObjectBorder(new OpenObjectDropDownBorder(emptyField));
                } else {
                    return new ObjectBorder(emptyField);
                }
            } else {
                return emptyField;
            }
        }

        public String getName() {
            return "empty field";
        }

        public boolean isAligned() {
            return false;
        }

        public boolean isOpen() {
            return false;
        }

        public boolean isReplaceable() {
            return true;
        }
        
        public boolean isResizeable() {
            return false;
        }

        public boolean isSubView() {
            return true;
        }
    }

    private final IconGraphic icon;
    private final TitleText text;

    public EmptyField(final Content content, final ViewSpecification specification, final ViewAxis axis, final Text style) {
        super(content, specification, axis);
        if (((ObjectContent) content).getObject() != null) {
            throw new IllegalArgumentException("Content for EmptyField must be null: " + content);
        }
        final NakedObject object = ((ObjectContent) getContent()).getObject();
        if (object != null) {
            throw new IllegalArgumentException("Content for EmptyField must be null: " + object);
        }
        icon = new IconGraphic(this, style);
        text = new EmptyFieldTitleText(this, style);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        int x = 0;
        final int y = icon.getBaseline();
        icon.draw(canvas, x, y);
        x += icon.getSize().getWidth();
        x += View.HPADDING;

        text.draw(canvas, x, y);
    }

    @Override
    public int getBaseline() {
        return icon.getBaseline();
    }

    @Override
    public Size getMaximumSize() {
        final Size size = icon.getSize();
        size.extendWidth(View.HPADDING);
        size.extendWidth(text.getSize().getWidth());
        return size;
    }

    private Consent canDrop(final NakedObject dragSource) {
        final ObjectContent content = (ObjectContent) getContent();
        return content.canSet(dragSource);
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        final Content sourceContent = drag.getSourceContent();
        if (sourceContent instanceof ObjectContent) {
            final NakedObject source = ((ObjectContent) sourceContent).getObject();
            final Consent canDrop = canDrop(source);
            if (canDrop.isAllowed()) {
                getState().setCanDrop();
            } else {
                getState().setCantDrop();
            }
            String actionText = canDrop.isVetoed() ? canDrop.getReason() : "Set to " + sourceContent.title();
            getFeedbackManager().setAction(actionText);
        } else {
            getState().setCantDrop();
        }

        markDamaged();
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        getState().clearObjectIdentified();
        markDamaged();
    }

    @Override
    public void drop(final ContentDrag drag) {
        getState().clearViewIdentified();
        markDamaged();
        final NakedObject target = ((ObjectContent) getParent().getContent()).getObject();
        final Content sourceContent = drag.getSourceContent();
        if (sourceContent instanceof ObjectContent) {
            final NakedObject source = ((ObjectContent) sourceContent).getObject();
            setField(target, source);
        }
    }

    /**
     * Objects returned by menus are used to set this field before passing the call on to the parent.
     */
    @Override
    public void objectActionResult(final NakedObject result, final Location at) {
        final NakedObject target = ((ObjectContent) getParent().getContent()).getObject();
        if (result instanceof NakedObject) {
            setField(target, result);
        }
        super.objectActionResult(result, at);
    }

    private void setField(final NakedObject parent, final NakedObject object) {
        if (canDrop(object).isAllowed()) {
            ((ObjectContent) getContent()).setObject(object);
            getParent().invalidateContent();
        }
    }

    @Override
    public String toString() {
        return "EmptyField" + getId();
    }
}
// Copyright (c) Naked Objects Group Ltd.
