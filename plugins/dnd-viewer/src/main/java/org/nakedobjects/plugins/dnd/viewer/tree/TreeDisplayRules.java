package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class TreeDisplayRules {
    private static boolean showCollectionsOnly = false;

    private TreeDisplayRules() {}

    public static void menuOptions(final UserActionSet options) {
        // TODO fix and remove following line
        if (true) {
            return;
        }

        final UserAction option = new UserAction() {
            public void execute(final Workspace workspace, final View view, final Location at) {
                showCollectionsOnly = !showCollectionsOnly;
            }

            public String getName(final View view) {
                return showCollectionsOnly ? "Show collections only" : "Show all references";
            }

            public Consent disabled(final View view) {
                return Allow.DEFAULT;
            }

            public String getDescription(final View view) {
                return "This option makes the system only show collections within the trees, and not single elements";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public String getHelp(final View view) {
                return "";
            }
        };
        options.add(option);
    }

    public static boolean isCollectionsOnly() {
        return showCollectionsOnly;
    }

    public static boolean canDisplay(final NakedObject object) {
        boolean lookupView = object != null && SpecificationFacets.isBoundedSet(object.getSpecification());
        final boolean showNonCollections = !TreeDisplayRules.isCollectionsOnly();
        final boolean objectView = object instanceof NakedObject && showNonCollections;
        final boolean collectionView = object.getSpecification().isCollection();
        return (objectView || collectionView) && !lookupView;
    }
}
// Copyright (c) Naked Objects Group Ltd.
