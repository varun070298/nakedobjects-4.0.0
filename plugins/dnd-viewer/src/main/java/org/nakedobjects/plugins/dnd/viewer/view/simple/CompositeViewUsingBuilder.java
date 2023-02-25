package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class CompositeViewUsingBuilder extends AbstractCompositeView {
    private final ViewBuilder builder;

    public CompositeViewUsingBuilder(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        builder = specification.getSubviewBuilder();
    }


    @Override
    public void debugStructure(final DebugString debug) {
        debug.appendln("Builder", builder);
        super.debugStructure(debug);
    }
    
    @Override
    public Size getMaximumSize() {
        final Size size = builder.getRequiredSize(this);
        size.extend(getPadding());
        size.ensureHeight(1);
        return size;
    }
    
    protected void buildView() {
            builder.build(getView());
    }

    protected void doLayout(Size maximumSize) {
            builder.layout(getView(), new Size(maximumSize));
    }
}
// Copyright (c) Naked Objects Group Ltd.
