package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public interface ColumnWidthStrategy {
    int getMinimumWidth(int i, NakedObjectAssociation specification);

    int getPreferredWidth(int i, NakedObjectAssociation specification);

    int getMaximumWidth(int i, NakedObjectAssociation specification);
}
// Copyright (c) Naked Objects Group Ltd.
