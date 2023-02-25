package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.focus.SubviewFocusManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public abstract class AbstractCompositeViewSpecification implements CompositeViewSpecification {
    protected ViewBuilder builder;

    public final View createView(final Content content, final ViewAxis axis) {
       // axis = axis();
        resolveObject(content);
        ViewAxis ax = builder.createViewAxis();
        if (ax == null) {
            ax = axis(content);
        }
        if (ax == null) {
            ax = axis;
        }
        View view = builder.createCompositeView(content, this, ax);
        return decorateView(view);
    }

    protected ViewAxis axis(Content content) {
        return null;
    }

    protected View decorateView(View view) {
        view.setFocusManager(new SubviewFocusManager(view));
        return view;
    }

    public ViewBuilder getSubviewBuilder() {
        return builder;
    }

    public View decorateSubview(final View subview) {
        return subview;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return true;
    }

    public boolean isSubView() {
        return false;
    }

    public boolean isAligned() {
        return false;
    }
    
    public boolean isResizeable() {
        return false;
    }

    protected void resolveObject(final Content content) {
        if (content instanceof ObjectContent) {
            final NakedObject object = ((ObjectContent) content).getObject();
            if (object != null && !object.getResolveState().isResolved()) {
                NakedObjectsContext.getPersistenceSession().resolveImmediately(object);
            }
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
