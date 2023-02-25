package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * Each option that a user is shown in an objects popup menu a MenuOption. A MenuOption details: the name of
 * an option (in the users language);
 * <ul>
 * the type of object that might result when requesting this option
 * </ul>; a way to determine whether a user can select this option on the current object.
 */
public abstract class AbstractUserAction implements UserAction {
    private String description;
    private String name;
    private final NakedObjectActionType type;

    public AbstractUserAction(final String name) {
        this(name, NakedObjectActionConstants.USER);
    }

    public AbstractUserAction(final String name, final NakedObjectActionType type) {
        this.name = name;
        this.type = type;
    }

    public Consent disabled(final View view) {
        return Allow.DEFAULT;
    }

    public abstract void execute(final Workspace workspace, final View view, final Location at);

    public String getDescription(final View view) {
        return description;
    }

    public String getHelp(final View view) {
        return "No help available for user action";
    }

    /**
     * Returns the stored name of the menu option.
     */
    public String getName(final View view) {
        return name;
    }

    public NakedObjectActionType getType() {
        return type;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("name", name);
        str.append("type", type);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
