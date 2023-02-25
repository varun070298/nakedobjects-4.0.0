package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.OneToManyField;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;


public class InternalCollectionBorder extends AbstractBorder {
    private final IconGraphic icon;

    protected InternalCollectionBorder(final View wrappedView) {
        super(wrappedView);

        icon = new InternalCollectionIconGraphic(this, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
        left = icon.getSize().getWidth();
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("InternalCollectionBorder ");
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        size.ensureWidth(left + 45 + right);
        size.ensureHeight(24);
        return size;
    }

    @Override
    public void draw(final Canvas canvas) {
        icon.draw(canvas, 0, getBaseline());

        final NakedObject collection = getContent().getNaked();
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        if (collection == null || facet.size(collection) == 0) {
            canvas.drawText("empty", left, getBaseline(), Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
        } else {
            final int x = icon.getSize().getWidth() / 2;
            final int x2 = x + 4;
            final int y = icon.getSize().getHeight() + 1;
            final int y2 = getSize().getHeight() - 5;
            canvas.drawLine(x, y, x, y2, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
            canvas.drawLine(x, y2, x2, y2, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        }
        super.draw(canvas);
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);
        // final NakedObjectSpecification nakedClass = ((OneToManyField) getContent()).getSpecification();
        // OptionFactory.addCreateOptions(nakedClass, options);
    }

    @Override
    public void objectActionResult(final NakedObject result, final Location at) {
        // same as in TreeNodeBorder
        final OneToManyField internalCollectionContent = (OneToManyField) getContent();
        final OneToManyAssociation field = internalCollectionContent.getOneToManyAssociation();
        final NakedObject target = ((ObjectContent) getParent().getContent()).getObject();

        final Consent valid = field.isValidToAdd(target, result);
        if (valid.isAllowed()) {
            field.addElement(target, result);
        }
        super.objectActionResult(result, at);
    }

    @Override
    public String toString() {
        return "InternalCollectionBorder/" + wrappedView;
    }
}
// Copyright (c) Naked Objects Group Ltd.
