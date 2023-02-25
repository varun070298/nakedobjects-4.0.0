package org.nakedobjects.plugins.html.task;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.ParseableEntryActionParameter;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;



public final class MethodTask extends Task {
    private final NakedObjectAction action;

    protected MethodTask(final Context context, final NakedObject target, final NakedObjectAction action) {
        super(context, action.getName(), action.getDescription(), target, action.getParameterCount());
        this.action = action;

        final NakedObjectActionParameter[] parameters = action.getParameters();
        final int len = parameters.length;

        for (int i = 0; i < len; i++) {
            names[i] = parameters[i].getName();
            descriptions[i] = parameters[i].getDescription();
            fieldSpecifications[i] = parameters[i].getSpecification();
            optional[i] = parameters[i].isOptional();

            if (parameters[i].getSpecification().isParseable()) {
                final ParseableEntryActionParameter valueParameter = (ParseableEntryActionParameter) parameters[i];
                noLines[i] = valueParameter.getNoLines();
                wraps[i] = valueParameter.canWrap();
                maxLength[i] = valueParameter.getMaximumLength();
                typicalLength[i] = valueParameter.getTypicalLineLength();
            }

        }

        // String[] names = action.getParameterNames();
        // String[] descriptions = action.getParameterDescriptions();
        // NakedObjectSpecification[] types = action.getParameterTypes();
        final NakedObject[] defaultParameterValues = action.getDefaults(target);
        // boolean[] optional = action.getOptionalParameters();
        for (int i = 0; i < names.length; i++) {
            // this.names[i] = names[i];
            // this.descriptions[i] = descriptions[i];
            // this.fieldSpecifications[i] = types[i];
            // this.optional[i] = optional[i];
            if (defaultParameterValues[i] == null) {
                // TODO use new promptForParameters method instead of all this
                if (action.isContributed()) {
                    initialState[i] = target;
                } else {
                    initialState[i] = null;
                }
            } else {
                initialState[i] = defaultParameterValues[i];
            }
            /*
             * noLines[i] = action.getParameterNoLines()[i]; wraps[i] = action.canParametersWrap()[i];
             * maxLength[i] = action.getParameterMaxLengths()[i]; typicalLength[i] =
             * action.getParameterTypicalLengths()[i];
             */
        }

    }

    @Override
    public void checkForValidity(final Context context) {
        final NakedObject[] parameters = getEntries(context);
        final NakedObject target = getTarget(context);
        final Consent consent = action.isProposedArgumentSetValid(target, parameters);
        error = consent.getReason();
    }

    @Override
    public NakedObject completeTask(final Context context, final Page page) {
        final NakedObject[] parameters = getEntries(context);
        final NakedObject target = getTarget(context);
        final NakedObject result = action.execute(target, parameters);
        final MessageBroker broker = NakedObjectsContext.getMessageBroker();
        final List<String> messages = broker.getMessages();
        final List<String> warnings = broker.getWarnings();
        context.setMessagesAndWarnings(messages, warnings);
        return result;
    }

    @Override
    public void debug(final DebugString debug) {
        debug.appendln("action: " + action);
        super.debug(debug);
    }

    @Override
    protected NakedObject[][] getOptions(final Context context, final int from, final int len) {
        final NakedObject[][] allOptions = action.getChoices(getTarget(context));
        final NakedObject[][] options = new NakedObject[len][];
        for (int i = from, j = 0; j < len; i++, j++) {
            options[j] = allOptions[i];
        }
        return options;
    }

    public boolean collectParameters() {
        // TODO use new promptForParameters method instead of all this

        final int expectedNoParameters = action.isContributed() ? 1 : 0;
        return action.getParameterCount() == expectedNoParameters;
    }
}

// Copyright (c) Naked Objects Group Ltd.
