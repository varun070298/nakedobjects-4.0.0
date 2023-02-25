package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public class DefaultColumnWidthStrategy implements ColumnWidthStrategy {

    private final int minimum;
    private final int preferred;
    private final int maximum;

    public DefaultColumnWidthStrategy() {
        this(18, 70, 250);
    }

    public DefaultColumnWidthStrategy(final int minimum, final int preferred, final int maximum) {
        if (minimum <= 0) {
            throw new IllegalArgumentException("minimum width must be greater than zero");
        }
        if (preferred <= minimum || preferred >= maximum) {
            throw new IllegalArgumentException("preferred width must be greater than minimum and less than maximum");
        }
        this.minimum = minimum;
        this.preferred = preferred;
        this.maximum = maximum;
    }

    public int getMinimumWidth(final int i, final NakedObjectAssociation specification) {
        return minimum;
    }

    public int getPreferredWidth(final int i, final NakedObjectAssociation specification) {
        return preferred;
    }

    public int getMaximumWidth(final int i, final NakedObjectAssociation specification) {
        return maximum;
    }
}
// Copyright (c) Naked Objects Group Ltd.
