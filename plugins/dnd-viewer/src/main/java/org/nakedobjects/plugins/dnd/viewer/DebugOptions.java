package org.nakedobjects.plugins.dnd.viewer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.MenuOptions;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugFrame;
import org.nakedobjects.plugins.dnd.viewer.debug.InfoDebugFrame;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.internal.monitor.SystemPrinter;


public class DebugOptions implements MenuOptions {
    private final XViewer viewer;

    public DebugOptions(final XViewer viewer) {
        this.viewer = viewer;
    }

    public void menuOptions(final UserActionSet options) {
        final String showExplorationMenu = "Always show exploration menu " + (viewer.showExplorationMenuByDefault ? "off" : "on");
        options.add(new AbstractUserAction(showExplorationMenu, UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                viewer.showExplorationMenuByDefault = !viewer.showExplorationMenuByDefault;
                view.markDamaged();
            }
        });

        final String repaint = "Show painting area  " + (viewer.showRepaintArea ? "off" : "on");
        options.add(new AbstractUserAction(repaint, UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                viewer.showRepaintArea = !viewer.showRepaintArea;
                view.markDamaged();
            }
        });

        final String debug = "Debug graphics " + (Toolkit.debug ? "off" : "on");
        options.add(new AbstractUserAction(debug, UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                Toolkit.debug = !Toolkit.debug;
                view.markDamaged();
            }
        });

        final String action = viewer.isShowingMouseSpy() ? "Hide" : "Show";
        options.add(new AbstractUserAction(action + " mouse spy", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                viewer.setShowMouseSpy(!viewer.isShowingMouseSpy());
            }
        });

        // I've commented this out because in the new design we should close the ExecutionContext
        // and then re-login.
//        options.add(new AbstractUserAction("Restart object loader/persistor", UserAction.DEBUG) {
//            @Override
//            public void execute(final Workspace workspace, final View view, final Location at) {
//                NakedObjectsContext.getObjectPersistor().reset();
//            }
//        });
        
        options.add(new AbstractUserAction("Diagnostics...", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final InfoDebugFrame f = new InfoDebugFrame();
                f.setInfo(new DebugInfo() {

                    public void debugData(DebugString debug) {
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        PrintStream out = new PrintStream(out2);
                        new SystemPrinter(out).printDiagnostics();
                        debug.append(out2.toString());
                    }

                    public String debugTitle() {
                        return "Diagnostics";
                    }
                    
                });
                f.show(at.getX() + 50, workspace.getBounds().getY() + 6);
            }
        });



        options.add(new AbstractUserAction("Debug system...", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final InfoDebugFrame f = new InfoDebugFrame();
                final DebugInfo[] contextInfo = NakedObjectsContext.debugSystem();
                f.setInfo(contextInfo);
                f.show(at.getX() + 50, workspace.getBounds().getY() + 6);
            }
        });


        options.add(new AbstractUserAction("Debug session...", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final InfoDebugFrame f = new InfoDebugFrame();
                final DebugInfo[] contextInfo = NakedObjectsContext.debugSession();
                f.setInfo(contextInfo);
                f.show(at.getX() + 50, workspace.getBounds().getY() + 6);
            }
        });

        options.add(new AbstractUserAction("Debug viewer...", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final InfoDebugFrame f = new InfoDebugFrame();
                f.setInfo(new DebugInfo[] { Toolkit.getViewFactory(), viewer.updateNotifier });
                f.show(at.getX() + 50, workspace.getBounds().getY() + 6);
            }
        });

        options.add(new AbstractUserAction("Debug overlay...", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final DebugFrame f = new OverlayDebugFrame(viewer);
                f.show(at.getX() + 50, workspace.getBounds().getY() + 6);
            }
        });

    }

}

// Copyright (c) Naked Objects Group Ltd.
