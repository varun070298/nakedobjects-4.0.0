package org.nakedobjects.plugins.dnd.viewer;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.plugins.dnd.MenuOptions;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugDumpSnapshotOption;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class LoggingOptions implements MenuOptions {

    public void menuOptions(final UserActionSet options) {
        options.add(loggingOption("Off", Level.OFF));
        options.add(loggingOption("Error", Level.ERROR));
        options.add(loggingOption("Warn", Level.WARN));
        options.add(loggingOption("Info", Level.INFO));
        options.add(loggingOption("Debug", Level.DEBUG));

        options.add(new DebugDumpSnapshotOption());
    }

    private AbstractUserAction loggingOption(final String name, final Level level) {
        return new AbstractUserAction("Log level " + level, UserAction.DEBUG) {
            @Override
            public Consent disabled(final View component) {
                return ConsentAbstract.allowIf(LogManager.getRootLogger().getLevel() != level);
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                LogManager.getRootLogger().setLevel(level);
            }
        };
    }

}

// Copyright (c) Naked Objects Group Ltd.
