package org.nakedobjects.plugins.html.action.view.util;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class MenuUtil {

    public static Component[] menu(final NakedObject target, final String targetObjectId, final Context context) {
        final NakedObjectSpecification specification = target.getSpecification();
        final NakedObjectAction[] actions1 = specification.getObjectActions(NakedObjectActionConstants.USER);
        final NakedObjectAction[] actions2 = specification.getObjectActions(NakedObjectActionConstants.EXPLORATION);
        final NakedObjectAction[] actions = new NakedObjectAction[actions1.length + actions2.length];
        System.arraycopy(actions1, 0, actions, 0, actions1.length);
        System.arraycopy(actions2, 0, actions, actions1.length, actions2.length);
        final Component[] menuItems = createMenu("Actions", target, actions, context, targetObjectId);
        return menuItems;
    }

    private static Component[] createMenu(
            final String menuName,
            final NakedObject target,
            final NakedObjectAction[] actions,
            final Context context,
            final String targetObjectId) {
        final List<Component> menuItems = new ArrayList<Component>();
        for (int j = 0; j < actions.length; j++) {
            final NakedObjectAction action = actions[j];
            final String name = action.getName();
            Component menuItem = null;
            if (action.getActions().length > 0) {
                final Component[] components = createMenu(name, target, action.getActions(), context, targetObjectId);
                menuItem = context.getComponentFactory().createSubmenu(name, components);
            } else {
                if (!action.isVisible(NakedObjectsContext.getAuthenticationSession(), target).isAllowed()) {
                    continue;
                }
                
                if (action.getType() == NakedObjectActionConstants.USER) {
                	// carry on, process this action
                } else if (action.getType() == NakedObjectActionConstants.EXPLORATION) {
                	//boolean isExploring = SessionAccess.inExplorationMode();
                	boolean isExploring = NakedObjectsContext.getDeploymentType().isExploring();
					if (isExploring) {
                		// carry on, process this action
                	} else {
                    	// ignore this action, skip onto next
                		continue;
                	}
                } else if (action.getType() == NakedObjectActionConstants.DEBUG) {
                	// TODO: show if debug "gesture" present
                } else {
                	// ignore this action, skip onto next
                	continue;
                }

                final String actionId = context.mapAction(action);
                boolean collectParameters;
                if (action.getParameterCount() == 0) {
                    collectParameters = false;
                    // TODO use new promptForParameters method instead of all this
                } else if (action.getParameterCount() == 1 && action.isContributed()
                        && target.getSpecification().isOfType(action.getParameters()[0].getSpecification())) {
                    collectParameters = false;
                } else {
                    collectParameters = true;
                }
                final Consent consent = action.isUsable(NakedObjectsContext.getAuthenticationSession(), target);
                final String consentReason = consent.getReason();
                menuItem = context.getComponentFactory().createMenuItem(
                        actionId, action.getName(), action.getDescription(),
                        consentReason, action.getType(), collectParameters, targetObjectId);
            }
            if (menuItem != null) {
                menuItems.add(menuItem);
            }
        }
        return menuItems.toArray(new Component[]{});
    }

}

// Copyright (c) Naked Objects Group Ltd.
