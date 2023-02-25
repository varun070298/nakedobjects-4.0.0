package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.basic.EmptyBorder;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.border.IconBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.ColumnLayout;
import org.nakedobjects.plugins.dnd.viewer.view.form.SummaryFormSpecification;


public class GridSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    public GridSpecification() {
        builder = new ColumnLayout(new CollectionElementBuilder(this), true);
    }

    public String getName() {
        return "Grid";
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isCollection() && requirement.is(ViewRequirement.OPEN);
    }

    public View createSubview(Content content, ViewAxis axis, int fieldNumber) {
        return new SummaryFormSpecification().createView(content, axis);
    }

    protected View decorateView(View view) {
        return new ColumnLabelBorder(super.decorateView(view));
    }

    public View decorateSubview(final View subview) {
        return new EmptyBorder(0, 0, 3, 0, new IconBorder(subview));
    }
}

class ColumnLabelBorder extends AbstractBorder {

    protected ColumnLabelBorder(View view) {
        super(view);

        left = 100;
    }

    public void draw(Canvas canvas) {
        View subview = getSubviews()[0];

        int top = subview.getPadding().getTop();
        for (View view : subview.getSubviews()) {
            String fieldName = ((FieldContent) view.getContent()).getFieldName();
            canvas.drawText(fieldName + ":", 0, view.getLocation().getY() + top + view.getBaseline(), Toolkit
                    .getColor(ColorsAndFonts.COLOR_PRIMARY1), Toolkit.getText(ColorsAndFonts.TEXT_LABEL));
            // canvas.drawRectangle(0, view.getLocation().getY() + top, 80, 10, Toolkit.getColor("primary1"));
        }

        super.draw(canvas);
    }
}

// Copyright (c) Naked Objects Group Ltd.
