package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class DroppableLabelBorder extends LabelBorder {

    public static View createObjectFieldLabelBorder(final View wrappedView) {
        final FieldContent fieldContent = (FieldContent) wrappedView.getContent();
        return new DroppableLabelBorder(fieldContent, wrappedView);
    }

    public static View createObjectParameterLabelBorder(final View wrappedView) {
        final ParameterContent parameterContent = (ParameterContent) wrappedView.getContent();
        return new DroppableLabelBorder(parameterContent, wrappedView);
    }

    private final ViewState labelState = new ViewState();
    private boolean overContent;


    public DroppableLabelBorder(FieldContent fieldContent, View wrappedView) {
        super(fieldContent, wrappedView);
    }

    public DroppableLabelBorder(ParameterContent fieldContent, View wrappedView) {
        super(fieldContent, wrappedView);
    }

    @Override
    public ViewAreaType viewAreaType(final Location location) {
        if (overBorder(location)) {
            return ViewAreaType.CONTENT; // used to ensure menu options for contained object are shown
        } else {
            return super.viewAreaType(location);
        }
    }

    @Override
    public void dragCancel(final InternalDrag drag) {
        super.dragCancel(drag);
        labelState.clearViewIdentified();
    }

    @Override
    public void drag(final ContentDrag drag) {
        final Location targetLocation = drag.getTargetLocation();
        if (overContent(targetLocation) && overContent == false) {
            overContent = true;
            super.dragIn(drag);
            dragOutOfLabel();
        } else if (overBorder(targetLocation) && overContent == true) {
            overContent = false;
            super.dragOut(drag);
            dragInToLabel(drag.getSourceContent());
        }

        super.drag(drag);
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        if (overContent(drag.getTargetLocation())) {
            super.dragIn(drag);
        } else {
            final Content sourceContent = drag.getSourceContent();
            dragInToLabel(sourceContent);
            markDamaged();
        }
    }

    private void dragInToLabel(final Content sourceContent) {
        overContent = false;
        final Consent canDrop = canDrop(sourceContent);
        if (canDrop.isAllowed()) {
            labelState.setCanDrop();
        } else {
            labelState.setCantDrop();
        }
        String actionText = canDrop.isVetoed() ? 
                            canDrop.getReason() : "Set to " + sourceContent.title();
        getFeedbackManager().setAction(actionText);
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        super.dragOut(drag);
        dragOutOfLabel();
    }

    private void dragOutOfLabel() {
        labelState.clearObjectIdentified();
        markDamaged();
    }

    @Override
    public void drop(final ContentDrag drag) {
        if (overContent(drag.getTargetLocation())) {
            super.drop(drag);
        } else {
            dragOutOfLabel();
            final Content sourceContent = drag.getSourceContent();
            if (canDrop(sourceContent).isAllowed()) {
                drop(sourceContent);
            }
        }
    }

    protected Consent canDrop(final Content dropContent) {
        if (dropContent instanceof ObjectContent) {
            final NakedObject source = ((ObjectContent) dropContent).getObject();
            final ObjectContent content = (ObjectContent) getContent();
            return content.canSet(source);
        } else {
            return Veto.DEFAULT;
        }
    }

    protected void drop(final Content dropContent) {
        if (dropContent instanceof ObjectContent) {
            final NakedObject object = ((ObjectContent) dropContent).getObject();
            ((ObjectContent) getContent()).setObject(object);
            getParent().invalidateContent();
        }
    }

    @Override
    protected Color textColor() {
        Color color;
        if (labelState.canDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_VALID);
        } else if (labelState.cantDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
        } else {
            color = super.textColor();
        }
        return color;
    }
}

// Copyright (c) Naked Objects Group Ltd.
