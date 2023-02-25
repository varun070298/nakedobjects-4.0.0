package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.LabelAxis;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class LabelBorder extends AbstractBorder {
    public static final int NORMAL = 0;
    public static final int DISABLED = 1;
    public static final int MANDATORY = 2;

    public static View createFieldLabelBorder(final View wrappedView) {
        final FieldContent fieldContent = (FieldContent) wrappedView.getContent();
        return new LabelBorder(fieldContent, wrappedView);
    }

    public static View createValueParameterLabelBorder(final View wrappedView) {
        final ParameterContent fieldContent = (ParameterContent) wrappedView.getContent();
        return new LabelBorder(fieldContent, wrappedView);
    }

    private final String label;
    private Text style;
    private Color color;

    protected LabelBorder(FieldContent fieldContent, View wrappedView) {
        super(wrappedView);
        if (fieldContent.isEditable().isVetoed()) {
            setDisabledStyling();
        } else if (fieldContent.isMandatory()) {
            setMandatoryStyling();
        } else {
            setOptionalStyling();
        }

        String name = fieldContent.getFieldName();
        this.label = name + ":";

        final int width = HPADDING + style.stringWidth(this.label) + HPADDING;
        if (getViewAxis() == null) {
            left = width;
        } else {
            ((LabelAxis) getViewAxis()).accommodateWidth(width);
        }
    }

    protected LabelBorder(ParameterContent fieldContent, View wrappedView) {
        super(wrappedView);
        if (fieldContent.isRequired()) {
            setMandatoryStyling();
        } else {
            setOptionalStyling();
        }

        String name = fieldContent.getParameterName();
        this.label = name + ":";

        final int width = HPADDING + style.stringWidth(this.label) + HPADDING;
        if (getViewAxis() == null) {
            left = width;
        } else {
            ((LabelAxis) getViewAxis()).accommodateWidth(width);
        }
    }

    private void setOptionalStyling() {
        style = Toolkit.getText(ColorsAndFonts.TEXT_LABEL);
        color = Toolkit.getColor(ColorsAndFonts.COLOR_LABEL);
    }

    private void setMandatoryStyling() {
        style = Toolkit.getText(ColorsAndFonts.TEXT_LABEL_MANDATORY);
        color = Toolkit.getColor(ColorsAndFonts.COLOR_LABEL_MANDATORY);
    }

    private void setDisabledStyling() {
        style = Toolkit.getText(ColorsAndFonts.TEXT_LABEL_DISABLED);
        color = Toolkit.getColor(ColorsAndFonts.COLOR_LABEL_DISABLED);
    }

    @Override
    protected int getLeft() {
        if (getViewAxis() == null) {
            return left;
        } else {
            return ((LabelAxis) getViewAxis()).getWidth();
        }
    }

    @Override
    public void debugDetails(final DebugString debug) {
        super.debugDetails(debug);
        debug.appendln("label", "'" + label + "'");
    }

    @Override
    public void draw(final Canvas canvas) {
        final Color color = textColor();
        canvas.drawText(label, left, wrappedView.getBaseline(), color, style);
        super.draw(canvas);
    }

    protected Color textColor() {
        return color;
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/" + ToString.name(this);
    }
}
// Copyright (c) Naked Objects Group Ltd.
