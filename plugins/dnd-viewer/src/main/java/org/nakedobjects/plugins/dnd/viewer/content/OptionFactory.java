package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.viewer.action.DisposeObjectOption;


public class OptionFactory {

    public static void addCreateOptions(final NakedObjectSpecification specification, final UserActionSet options) {
        NakedObjectAction[] actions;
        // TODO do the same as addObjectMenuOptions and collect together all the
        // actions for all the types
        actions = specification.getServiceActionsFor(NakedObjectActionConstants.USER, NakedObjectActionConstants.EXPLORATION,
                NakedObjectActionConstants.DEBUG);
        menuOptions(actions, null, options);
    }

    public static void addObjectMenuOptions(final NakedObject adapter, final UserActionSet options) {
        if (adapter == null) {
            return;
        }

        NakedObjectSpecification noSpec = adapter.getSpecification();
        menuOptions(noSpec.getObjectActions(NakedObjectActionConstants.USER, NakedObjectActionConstants.EXPLORATION,
                NakedObjectActionConstants.DEBUG), adapter, options);

        // TODO: this looks like a bit of a hack; can we improve it by looking at the facets?
        if (adapter.getObject() instanceof NakedObjectList) {
            return;
        }
        Oid oid = adapter.getOid();
        if (oid != null && oid.isTransient()) {
            return;
        }
        if (noSpec.isService()) {
            return;
        }

        options.add(new DisposeObjectOption());
    }

    private static void menuOptions(final NakedObjectAction[] actions, final NakedObject target, final UserActionSet menuOptionSet) {
        for (int i = 0; i < actions.length; i++) {
            UserAction option = null;
            if (actions[i].getActions().length > 0) {
                option = new UserActionSet(actions[i].getName(), menuOptionSet);
                menuOptions(actions[i].getActions(), target, (UserActionSet) option);

            } else {
                final int noOfParameters = actions[i].getParameterCount();
                if (noOfParameters == 0) {
                    option = ImmediateObjectOption.createOption(actions[i], target);
                } else if (actions[i].isContributed() && noOfParameters == 1 && target != null
                        && target.getSpecification().isOfType(actions[i].getParameters()[0].getSpecification())) {
                    option = ImmediateObjectOption.createServiceOption(actions[i], target);
                } else {
                    option = DialoggedObjectOption.createOption(actions[i], target);
                }
            }
            if (option != null) {
                menuOptionSet.add(option);
            }
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
