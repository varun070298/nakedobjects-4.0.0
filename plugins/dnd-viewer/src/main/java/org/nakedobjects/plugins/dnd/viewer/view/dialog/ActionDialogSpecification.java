package org.nakedobjects.plugins.dnd.viewer.view.dialog;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.ActionContent;
import org.nakedobjects.plugins.dnd.BackgroundTask;
import org.nakedobjects.plugins.dnd.BackgroundWork;
import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.LabelAxis;
import org.nakedobjects.plugins.dnd.ObjectParameter;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.TextParseableParameter;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewFactory;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractButtonAction;
import org.nakedobjects.plugins.dnd.viewer.action.CancelAction;
import org.nakedobjects.plugins.dnd.viewer.border.ButtonBorder;
import org.nakedobjects.plugins.dnd.viewer.border.DroppableLabelBorder;
import org.nakedobjects.plugins.dnd.viewer.border.IconBorder;
import org.nakedobjects.plugins.dnd.viewer.border.LabelBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ActionDialogSpecification extends AbstractCompositeViewSpecification {
    private static final Logger LOG = Logger.getLogger(ActionDialogSpecification.class);

    private static class DialogFormSubviews implements SubviewSpec {

        public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
            if (content instanceof TextParseableParameter) {
                final ViewFactory factory = Toolkit.getViewFactory();
                return factory.createView(new ViewRequirement(content, axis, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW));
            } else if (content instanceof ObjectParameter) {
                final ViewFactory factory = Toolkit.getViewFactory();
                return factory.createView(new ViewRequirement(content, axis, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW));
            }

            return null;
        }

        public View decorateSubview(final View fieldView) {
            if (fieldView.getContent() instanceof ObjectParameter) {
                return DroppableLabelBorder.createObjectParameterLabelBorder(fieldView);
            } else {
                return LabelBorder.createValueParameterLabelBorder(fieldView);
            }
        }
    }

    private static class ExecuteAction extends AbstractButtonAction {
        public ExecuteAction() {
            this("Apply");
        }

        public ExecuteAction(final String name) {
            super(name, true);
        }

        @Override
        public Consent disabled(final View view) {
            final View[] subviews = view.getSubviews();
            final StringBuffer missingFields = new StringBuffer();
            final StringBuffer invalidFields = new StringBuffer();
            for (int i = 0; i < subviews.length; i++) {
                final View field = subviews[i];
                final ParameterContent content = ((ParameterContent) field.getContent());
                final boolean isEmpty = content.getNaked() == null;
                if (content.isRequired() && isEmpty) {
                    final String parameterName = content.getParameterName();
                    if (missingFields.length() > 0) {
                        missingFields.append(", ");
                    }
                    missingFields.append(parameterName);

                } else if (field.getState().isInvalid()) {
                    final String parameterName = content.getParameterName();
                    if (invalidFields.length() > 0) {
                        invalidFields.append(", ");
                    }
                    invalidFields.append(parameterName);
                }
            }
            if (missingFields.length() > 0) {
                // TODO: move logic into Facet
                return new Veto(String.format("Fields needed: %s", missingFields));
            }
            if (invalidFields.length() > 0) {
                // TODO: move logic into Facet
                return new Veto(String.format("Invalid fields: %s", invalidFields));
            }

            final ActionContent actionContent = ((ActionContent) view.getContent());
            return actionContent.disabled();
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            final BackgroundTask task = new BackgroundTask() {
                public void execute() {
                    ActionContent actionContent = ((ActionContent) view.getContent());
                    NakedObject result = actionContent.execute();
                    LOG.debug("action invoked with result " + result);
                    if (result != null) {
                        Location dialogLocation = view.getAbsoluteLocation();
                        move(dialogLocation);
                        workspace.objectActionResult(result, dialogLocation);
                    }
                    view.getViewManager().disposeUnneededViews();
                    view.getFeedbackManager().showMessagesAndWarnings();
                }

                public String getName() {
                    return ((ActionContent) view.getContent()).getActionName();
                }

                public String getDescription() {
                    return "Running action " + getName() + " on  " + view.getContent().getNaked();
                }
            };
            LOG.debug("  ... created task " + task);

            BackgroundWork.runTaskInBackground(view, task);
        }

        protected void move(final Location at) {
            at.move(30, 60);
        }
    }

    private static class ExecuteAndCloseAction extends ExecuteAction {
        public ExecuteAndCloseAction() {
            super("OK");
        }

        @Override
        public void execute(final Workspace workspace, final View view, final Location at) {
            LOG.debug("executing action " + this);
            view.dispose();
            LOG.debug("  ... disposed view, now executing");
            super.execute(workspace, view, at);
            view.getViewManager().setKeyboardFocus(workspace);
            // view.getViewManager().clearKeyboardFocus();
        }

        @Override
        protected void move(final Location at) {}
    }

    public ActionDialogSpecification() {
        builder = new StackLayout(new ActionFieldBuilder(new DialogFormSubviews()));
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content instanceof ActionContent;
    }


    @Override
    protected ViewAxis axis(Content content) {
        return new LabelAxis();
    }

    @Override
    protected View decorateView(View view) {
        // TODO reintroduce the 'Apply' notion, but under control from the method declaration
        final ButtonAction[] actions = new ButtonAction[] { new ExecuteAndCloseAction(), new CancelAction() };
        final ButtonBorder buttonBorder = new ButtonBorder(actions, new IconBorder(view));
        buttonBorder.setFocusManager(new ActionDialogFocusManager(buttonBorder));
        return buttonBorder;
    }

    public String getName() {
        return "Action Dialog";
    }

}
// Copyright (c) Naked Objects Group Ltd.
