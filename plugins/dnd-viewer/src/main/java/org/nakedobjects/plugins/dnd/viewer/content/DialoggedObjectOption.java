package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.plugins.dnd.BackgroundTask;
import org.nakedobjects.plugins.dnd.BackgroundWork;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * Options for an underlying object determined dynamically by looking for methods starting with action, veto
 * and option for specifying the action, vetoing the option and giving the option an name respectively.
 */
public class DialoggedObjectOption extends AbstractObjectOption {
    public static DialoggedObjectOption createOption(final NakedObjectAction action, final NakedObject object) {
        final int paramCount = action.getParameterCount();
        Assert.assertTrue("Only for actions taking one or more params", paramCount > 0);
        if (!action.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()
                || !action.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
            return null;
        }

        final DialoggedObjectOption option = new DialoggedObjectOption(action, object);
        return option;
    }

    private DialoggedObjectOption(final NakedObjectAction action, final NakedObject target) {
        super(action, target, action.getName() + "...");
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        BackgroundWork.runTaskInBackground(view, new BackgroundTask() {
            public void execute() {
                final ActionHelper helper = ActionHelper.createInstance(target, action);
                Content content;
                if (target == null && action.getOnType().isService() || target != null && target.getSpecification().isObject()) {
                    content = new ObjectActionContent(helper);
                } else if (target.getSpecification().isCollection()) {
                    content = new CollectionActionContent(helper);
                } else {
                    throw new UnknownTypeException(target);
                }
                final View dialog = Toolkit.getViewFactory().createDialog(content);
                final Location loc = view.getAbsoluteLocation();
                dialog.setLocation(loc);
                workspace.addDialog(dialog);
            }

            public String getDescription() {
                return "Preparing action " + getName() + " on  " + view.getContent().getNaked();
            }

            public String getName() {
                return "Preparing action " + action.getName();
            }
        });
    }
}
// Copyright (c) Naked Objects Group Ltd.
