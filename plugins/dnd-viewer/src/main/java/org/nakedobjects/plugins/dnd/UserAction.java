package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public interface UserAction {
    public static final NakedObjectActionType USER = NakedObjectActionConstants.USER;
    public static final NakedObjectActionType DEBUG = NakedObjectActionConstants.DEBUG;
    public static final NakedObjectActionType EXPLORATION = NakedObjectActionConstants.EXPLORATION;

    /**
     * Returns the type of action: user, exploration, debug, or a set.
     */
    NakedObjectActionType getType();

    /**
     * Indicate that this action is disabled
     */
    Consent disabled(View view);

    /**
     * Invoke this action.
     */
    void execute(Workspace workspace, View view, Location at);

    /**
     * Returns the description of the action.
     */
    String getDescription(View view);

    /**
     * Returns the help text for the action.
     */
    String getHelp(View view);

    /**
     * Returns the name of the action as the user will refer to it.
     */
    String getName(View view);
}
// Copyright (c) Naked Objects Group Ltd.
