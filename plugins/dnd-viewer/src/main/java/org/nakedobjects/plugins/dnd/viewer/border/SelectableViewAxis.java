package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.tree.MasterDetailPanel;

public class SelectableViewAxis implements ViewAxis {
    private View selectedView;
    private MasterDetailPanel target;

    public SelectableViewAxis(MasterDetailPanel masterDetailPanel) {
        target = masterDetailPanel;
    }

    public void selected(View view) {
        selectedView = view;
        target.setSelectedNode(selectedView);
    }

    public boolean isSelected(View view) {
        return selectedView == view;
    }
}


// Copyright (c) Naked Objects Group Ltd.
