package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.plugins.dnd.BackgroundTask;
import org.nakedobjects.plugins.dnd.BackgroundWork;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * Options for an underlying object determined dynamically by looking for methods starting with action, veto
 * and option for specifying the action, vetoing the option and giving the option an name respectively.
 */
public class ImmediateObjectOption extends AbstractObjectOption {

    public static ImmediateObjectOption createOption(final NakedObjectAction action, final NakedObject object) {
        Assert.assertTrue("Only suitable for 0 param methods", action.getParameterCount() == 0);
        if (!action.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
            return null;
        }
        final ImmediateObjectOption option = new ImmediateObjectOption(action, object);
        return option;
    }

    public static ImmediateObjectOption createServiceOption(final NakedObjectAction action, final NakedObject object) {
        Assert.assertTrue("Only suitable for 1 param methods", action.getParameterCount() == 1);
        if (!action.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
            return null;
        }
        final ImmediateObjectOption option = new ImmediateObjectOption(action, object);

        return option;
    }

    private ImmediateObjectOption(final NakedObjectAction action, final NakedObject target) {
        super(action, target, action.getName());
    }

    @Override
    protected Consent checkValid() {
        return action.isProposedArgumentSetValid(target, null);
    }

    // TODO this method is very similar to ActionDialogSpecification.execute()
    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        BackgroundWork.runTaskInBackground(view, new BackgroundTask() {
            public void execute() {
                NakedObject result;
                result = action.execute(target, null);
                view.objectActionResult(result, at);
                view.getViewManager().disposeUnneededViews();
                view.getFeedbackManager().showMessagesAndWarnings();
            }

            public String getDescription() {
                return "Running action " + getName() + " on  " + view.getContent().getNaked();
            }

            public String getName() {
                return "NakedObjectAction " + action.getName();
            }
        });
    }
}
// Copyright (c) Naked Objects Group Ltd.
