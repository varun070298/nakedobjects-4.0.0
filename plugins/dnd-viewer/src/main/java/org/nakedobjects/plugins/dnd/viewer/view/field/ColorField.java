package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.value.ColorValueFacet;
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
import org.nakedobjects.plugins.dnd.viewer.border.DisposeOverlay;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class ColorField extends TextParseableFieldAbstract {
    public static class Specification extends AbstractFieldSpecification {

        @Override
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return content.isTextParseable() && content.getSpecification().getFacet(ColorValueFacet.class) != null;
        }

        public View createView(final Content content, final ViewAxis axis) {
            return new ColorField(content, this, axis);
        }

        public String getName() {
            return "Color";
        }
    }

    private int color;

    public ColorField(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }
    
    @Override
    public void draw(final Canvas canvas) {
        Color color;

        if (hasFocus()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
        } else if (getParent().getState().isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
        } else if (getParent().getState().isRootViewIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2);
        } else {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        }

        int top = 0;
        int left = 0;

        final Size size = getSize();
        int w = size.getWidth() - 1;
        int h = size.getHeight() - 1;
        canvas.drawRectangle(left, top, w, h, color);
        left++;
        top++;
        w -= 1;
        h -= 1;
        canvas.drawSolidRectangle(left, top, w, h, Toolkit.getColor(getColor()));
    }

    @Override
    public void firstClick(final Click click) {
        if (((TextParseableContent) getContent()).isEditable().isAllowed()) {
            final View overlay = new DisposeOverlay(new ColorFieldOverlay(this));
            final Location location = this.getAbsoluteLocation();
            // Location location = click.getLocationWithinViewer();
            // TODO offset by constant amount
            // location.move(10, 10);
            overlay.setLocation(location);
            // overlay.setSize(overlay.getRequiredSize(new Size()));
            // overlay.markDamaged();
            getViewManager().setOverlayView(overlay);
        }
    }

    @Override
    public int getBaseline() {
        return VPADDING + Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent();
    }

    int getColor() {
        final TextParseableContent content = ((TextParseableContent) getContent());
        final ColorValueFacet col = content.getSpecification().getFacet(ColorValueFacet.class);
        return col.colorValue(content.getNaked());
    }

    @Override
    public Size getMaximumSize() {
        return new Size(45, 15);
    }

    @Override
    protected void save() {
        try {
            parseEntry("" + color);
        } catch (final InvalidEntryException e) {
            throw new NotYetImplementedException();
        }
    }

    void setColor(final int color) {
        this.color = color;
        initiateSave(false);
    }
}
// Copyright (c) Naked Objects Group Ltd.
