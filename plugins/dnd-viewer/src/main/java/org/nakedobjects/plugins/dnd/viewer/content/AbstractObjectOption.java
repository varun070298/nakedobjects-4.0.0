package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public abstract class AbstractObjectOption extends AbstractUserAction {
    protected final NakedObjectAction action;
    protected final NakedObject target;

    protected AbstractObjectOption(final NakedObjectAction action, final NakedObject target, final String name) {
        super(name);
        this.action = action;
        this.target = target;
    }

    @Override
    public Consent disabled(final View view) {
        final NakedObject adapter = view.getContent().getNaked();
        if (adapter != null && adapter.getResolveState().isDestroyed()) {
            // TODO: move logic into Facet
            return new Veto("Can't do anything with a destroyed object");
        }
        final Consent usableForUser = action.isUsable(NakedObjectsContext.getAuthenticationSession(), target);
        if (usableForUser.isVetoed()) {
            return usableForUser;
        }

        final Consent validParameters = checkValid();
        if (validParameters != null && validParameters.isVetoed()) {
            return validParameters;
        }
        final String desc = action.getDescription();
        final String description = getName(view) + (desc.length() == 0 ? "" : ": " + desc);
        // TODO: replace with a Facet
        return new Allow(description);
    }

    protected Consent checkValid() {
        return null;
    }

    @Override
    public String getHelp(final View view) {
        return action.getHelp();
    }

    @Override
    public NakedObjectActionType getType() {
        return action.getType();
    }

    @Override
    public String toString() {
        return new ToString(this).append("action", action).toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
