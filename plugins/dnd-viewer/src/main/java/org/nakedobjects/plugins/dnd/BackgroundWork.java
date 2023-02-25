package org.nakedobjects.plugins.dnd;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectApplicationException;


public final class BackgroundWork {
    private static final Logger LOG = Logger.getLogger(BackgroundTask.class);

    private static class BackgroundThread extends Thread {
        private final View view;
        private final BackgroundTask task;

        public BackgroundThread(View view, BackgroundTask task) {
            super("nof-background");
            this.view = view;
            this.task = task;
            LOG.debug("creating background thread for task " + task);
        }

        @Override
        public void run() {
            try {
                view.getState().setActive();
                view.getFeedbackManager().setBusy(view, task);
                scheduleRepaint(view);

                LOG.debug("running background thread for task " + task);
                task.execute();

            } catch (Throwable e) {
                if (!(e instanceof NakedObjectApplicationException)) {
                    String message = "Error while running background task " + task.getName();
                    LOG.error(message, e);
                }
                view.getFeedbackManager().showException(e);

            } finally {
                view.getState().setInactive();
                view.getFeedbackManager().clearBusy(view);
                scheduleRepaint(view);
            }
        }

        private static void scheduleRepaint(final View view) {
            view.markDamaged();
            view.getViewManager().scheduleRepaint();
        }

    }

    public static void runTaskInBackground(final View view, final BackgroundTask task) {
        final Thread t = new BackgroundThread(view, task);
        t.start();
    }

    private BackgroundWork() {}

}
// Copyright (c) Naked Objects Group Ltd.
