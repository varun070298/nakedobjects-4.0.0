package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


/*
 * TODO this class does not set the underlying business object  via its boolean adapter.  Need
 * to create an content type for flags.
 */
public class CheckboxField extends AbstractField {
    private static final int size = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getTextHeight();

    public static class Specification extends AbstractFieldSpecification {
        @Override
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return content.isTextParseable() && content.getSpecification().containsFacet(BooleanValueFacet.class);
        }

        public View createView(final Content content, final ViewAxis axis) {
            return new CheckboxField(content, this, axis);
        }

        public String getName() {
            return "Checkbox";
        }
    }

    public CheckboxField(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public void draw(final Canvas canvas) {
        Color color;
        color = getIdentified() ? Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2) : null;
        color = hasFocus() ? Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED) : color;

        final int top = VPADDING;
        final int left = HPADDING;
        if (color != null) {
            canvas.drawRectangle(left - 2, top - 2, size + 4, size + 4, color);
        }

        color = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        canvas.drawRectangle(left, top, size, size, color);
        if (isSet()) {

            final Shape tick = new Shape(0, 6);
            // tick.addVertex(12, 8);
            tick.addVertex(4, 12);
            // tick.addVertex(6, 8);
            // tick.addVertex(8, 5);
            tick.addVertex(12, 0);
            // tick.addVertex(8, 4);
            // tick.addVertex(4, 7);
            tick.addVertex(4, 8);

            canvas.drawSolidShape(tick, 3, 3, color);

            /*
             * left += 2; top += 2; int bottom = size - 1; int right = size - 2; canvas.drawLine(left, top,
             * right, bottom, color); canvas.drawLine(left + 1, top, right + 1, bottom, color);
             * canvas.drawLine(right, top, left, bottom, color); canvas.drawLine(right + 1, top, left + 1,
             * bottom, color);
             */
        }
    }

    @Override
    public void firstClick(final Click click) {
        toggle();
    }

    @Override
    public void keyTyped(final char keyCode) {
        if (keyCode == ' ') {
            toggle();
        } else {
            super.keyTyped(keyCode);
        }
    }

    private void toggle() {
        if (canChangeValue().isAllowed()) {
            initiateSave(false);
        }
    }

    @Override
    public Consent canChangeValue() {
        final TextParseableContent cont = (TextParseableContent) getContent();
        return cont.isEditable();
    }

    @Override
    public int getBaseline() {
        return VPADDING + Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent();
    }

    @Override
    public Size getMaximumSize() {
        return new Size(HPADDING + size + HPADDING, VPADDING + size + VPADDING);
    }

    private boolean isSet() {
        final BooleanValueFacet booleanValueFacet = getContent().getSpecification().getFacet(BooleanValueFacet.class);
        return booleanValueFacet.isSet(getContent().getNaked());
    }

    @Override
    protected void save() {
        final BooleanValueFacet booleanValueFacet = getContent().getSpecification().getFacet(BooleanValueFacet.class);
        final NakedObject naked = getContent().getNaked();
        if (naked == null) {
            getContent().parseTextEntry("true");
        } else {
            booleanValueFacet.toggle(naked);
        }

        // return parsed != null ? PersistorUtil.createAdapter(parsed) : null;

        markDamaged();
        ((TextParseableContent) getContent()).entryComplete();
        getParent().invalidateContent();
    }
}
// Copyright (c) Naked Objects Group Ltd.
