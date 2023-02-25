package org.nakedobjects.plugins.dnd.viewer.debug;

import static org.nakedobjects.metamodel.commons.lang.CastUtils.enumerationOver;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.logging.SnapshotAppender;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * Display debug window
 */
public class DebugDumpSnapshotOption extends AbstractUserAction {
    public DebugDumpSnapshotOption() {
        super("Dump log snapshot", UserAction.DEBUG);
    }

    @Override
    public Consent disabled(final View component) {
        final Enumeration<Logger> enumeration = enumerationOver(Logger.getRootLogger().getAllAppenders(), Logger.class);
        while (enumeration.hasMoreElements()) {
            final Appender appender = (Appender) enumeration.nextElement();
            if (appender instanceof SnapshotAppender) {
                return Allow.DEFAULT;
            }
        }
        // TODO: move logic into Facet
        return new Veto("No available snapshot appender");
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final Enumeration<Logger> enumeration = enumerationOver(Logger.getRootLogger().getAllAppenders(), Logger.class);
        while (enumeration.hasMoreElements()) {
            final Appender appender = (Appender) enumeration.nextElement();
            if (appender instanceof SnapshotAppender) {
                ((SnapshotAppender) appender).forceSnapshot();
            }
        }
    }

    @Override
    public String getDescription(final View view) {
        return "Force a snapshot of the log";
    }
}
// Copyright (c) Naked Objects Group Ltd.
