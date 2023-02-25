package org.nakedobjects.plugins.dnd.viewer.action;

import org.apache.log4j.Logger;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ReplaceViewOption extends AbstractUserAction {
    private static final Logger LOG = Logger.getLogger(ReplaceViewOption.class);
    private final ViewSpecification specification;

    public ReplaceViewOption(final ViewSpecification specification) {
        super("View as " + specification.getName());
        this.specification = specification;
    }

    @Override
    public String getDescription(final View view) {
        return "Replace this " + view.getSpecification().getName() + " view with a " + specification.getName() + " view";
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final View replacement = specification.createView(view.getContent(), view.getViewAxis());
        // CompositeViewSpecification compositeViewSpecification = (CompositeViewSpecification)
        // view.getParent().getSpecification();
        // replacement = compositeViewSpecification.getSubviewBuilder().decorateSubview(replacement);
        LOG.debug("replacement view " + replacement);
        view.getParent().replaceView(view.getView(), replacement);
    }

    @Override
    public String toString() {
        return super.toString() + " [prototype=" + specification.getName() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
