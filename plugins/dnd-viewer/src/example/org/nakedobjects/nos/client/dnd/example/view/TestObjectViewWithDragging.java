package org.nakedobjects.viewer.dnd.example.view;

import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.Drag;
import org.nakedobjects.viewer.dnd.DragStart;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;

import org.apache.log4j.Logger;


public class TestObjectViewWithDragging extends TestObjectView {

    private static final Logger LOG = Logger.getLogger(TestObjectViewWithDragging.class);

    public TestObjectViewWithDragging(final 
            Content content, final
            ViewSpecification specification, final
            ViewAxis axis, final
            int width, final
            int height, final
            String label) {
        super(content, specification, axis, width, height, label);
    }

    public Drag dragStart(final DragStart drag) {
        LOG.debug("drag start " + drag.getLocation());
        return super.dragStart(drag);
    }
}
// Copyright (c) Naked Objects Group Ltd.
