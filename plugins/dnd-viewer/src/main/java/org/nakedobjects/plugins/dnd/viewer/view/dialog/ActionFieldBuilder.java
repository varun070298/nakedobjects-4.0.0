package org.nakedobjects.plugins.dnd.viewer.view.dialog;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.ActionContent;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectParameter;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.TextParseableParameter;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.content.ObjectParameterImpl;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;


public class ActionFieldBuilder extends AbstractViewBuilder {
    private static final Logger LOG = Logger.getLogger(ActionFieldBuilder.class);
    private final SubviewSpec subviewDesign;

    public ActionFieldBuilder(final SubviewSpec subviewDesign) {
        this.subviewDesign = subviewDesign;
    }

    @Override
    public void build(final View view) {
        Assert.assertEquals(view.getView(), view);

        final ActionContent actionContent = ((ActionContent) view.getContent());
        if (view.getSubviews().length == 0) {
            newBuild(view, actionContent);
        } else {
            updateBuild(view, actionContent);
        }

    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        return new CompositeViewUsingBuilder(content, specification, axis);
    }

    private View createFieldView(final View view, final ParameterContent parameter, int sequence) {
        final View fieldView = subviewDesign.createSubview(parameter, view.getViewAxis(), sequence);
        if (fieldView == null) {
            throw new NakedObjectException("All parameters must be shown");
        }
        return fieldView;
    }

    @Override
    public View decorateSubview(final View subview) {
        return subviewDesign.decorateSubview(subview);
    }

    private void newBuild(final View view, final ActionContent actionContent) {
        LOG.debug("build new view " + view + " for " + actionContent);
        final int noParameters = actionContent.getNoParameters();
        View focusOn = null;
        for (int f = 0; f < noParameters; f++) {
            final ParameterContent parameter = actionContent.getParameterContent(f);
            final View fieldView = createFieldView(view, parameter, f);
            final View decoratedSubview = decorateSubview(fieldView);
            view.addView(decoratedSubview);

            // set focus to first value field
            if (focusOn == null && parameter instanceof TextParseableParameter && fieldView.canFocus()) {
                focusOn = decoratedSubview;
            }
        }

        if (focusOn != null) {
            view.getViewManager().setKeyboardFocus(focusOn);
        }
    }

    private void updateBuild(final View view, final ActionContent actionContent) {
        LOG.debug("rebuild view " + view + " for " + actionContent);
        final View[] subviews = view.getSubviews();

        for (int i = 0; i < subviews.length; i++) {
            final View subview = subviews[i];
            final Content content = subview.getContent();

            final NakedObject subviewsObject = subview.getContent().getNaked();
            final NakedObject invocationsObject = ((ActionContent) view.getContent()).getParameterObject(i);

            if (content instanceof ObjectParameter) {
                if (subviewsObject != invocationsObject) {
                    final ObjectParameter parameter = new ObjectParameterImpl((ObjectParameterImpl) content, invocationsObject);
                    final View fieldView = createFieldView(view, parameter, i);
                    view.replaceView(subview, decorateSubview(fieldView));
                }
            } else {
                subview.refresh();
            }
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
