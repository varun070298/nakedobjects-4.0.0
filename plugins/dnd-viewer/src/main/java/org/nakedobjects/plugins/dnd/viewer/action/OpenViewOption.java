package org.nakedobjects.plugins.dnd.viewer.action;

import org.apache.log4j.Logger;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class OpenViewOption extends AbstractUserAction {
    private static final Logger LOG = Logger.getLogger(OpenViewOption.class);
    private final ViewSpecification specification;

    public OpenViewOption(final ViewSpecification builder) {
        super("Open as " + builder.getName());
        this.specification = builder;
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final View newView = specification.createView(view.getContent(), null);
        LOG.debug("open view " + newView);
        newView.setLocation(at);
        workspace.addWindow(newView);
        workspace.markDamaged();
    }

    @Override
    public String getDescription(final View view) {
        final String title = view.getContent().title();
        return "Open '" + title + "' in a " + specification.getName() + " window";
    }

    @Override
    public String toString() {
        return super.toString() + " [prototype=" + specification.getName() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
