package org.nakedobjects.plugins.html.servlet.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectApplicationException;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.action.ChangeContext;
import org.nakedobjects.plugins.html.action.LogOut;
import org.nakedobjects.plugins.html.action.Welcome;
import org.nakedobjects.plugins.html.action.edit.AddItemToCollection;
import org.nakedobjects.plugins.html.action.edit.EditObject;
import org.nakedobjects.plugins.html.action.edit.RemoveItemFromCollection;
import org.nakedobjects.plugins.html.action.edit.Save;
import org.nakedobjects.plugins.html.action.misc.About;
import org.nakedobjects.plugins.html.action.misc.SetUser;
import org.nakedobjects.plugins.html.action.misc.SwapUser;
import org.nakedobjects.plugins.html.action.view.CollectionView;
import org.nakedobjects.plugins.html.action.view.FieldCollectionView;
import org.nakedobjects.plugins.html.action.view.ObjectView;
import org.nakedobjects.plugins.html.action.view.ServiceView;
import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentFactory;
import org.nakedobjects.plugins.html.component.DebugPane;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.context.ObjectLookupException;
import org.nakedobjects.plugins.html.crumb.Crumb;
import org.nakedobjects.plugins.html.image.ImageLookup;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.plugins.html.task.InvokeMethod;
import org.nakedobjects.plugins.html.task.TaskLookupException;
import org.nakedobjects.plugins.html.task.TaskStep;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.system.internal.monitor.Monitor;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.util.Dump;



public class WebController {
    private static final String ERROR_REASON = "This error occurs when you go back to a page "
            + "using the browsers back button.  To avoid this error in the future please avoid using the back button";

    private class DebugView implements Action {
        public void execute(final Request request, final Context context, final Page page) {
            page.setTitle("Debug");

            final DebugPane debugPane = context.getComponentFactory().createDebugPane();
            page.setDebug(debugPane);

            final DebugString debug = new DebugString();


            AuthenticationSession authenticationSession = NakedObjectsContext.getAuthenticationSession();
            debug.appendTitle("Session");
            if (authenticationSession != null) {
                debug.appendln("user", authenticationSession.getUserName());
                debug.appendln("roles", authenticationSession.getRoles());
            } else {
                debug.appendln("none");
            }

            final UserProfile userProfile = NakedObjectsContext.getUserProfile();
            debug.appendTitle("User profile");
            if (userProfile != null) {
                userProfile.debugData(debug);
            } else {
                debug.appendln("none");
            }

            debug.appendTitle("Actions");
            final Iterator e = actions.entrySet().iterator();
            debug.indent();
            while (e.hasNext()) {
                final Map.Entry element = (Map.Entry) e.next();
                debug.appendln(element.getKey() + " -> " + element.getValue());
            }
            debug.unindent();

            context.debug(debug);

            ImageLookup.debug(debug);

            debugPane.appendln("<pre>" + debug.toString() + "</pre>");
        }

        public String name() {
            return "debug";
        }
    }

    private class DebugSpecification implements Action {
        public void execute(final Request request, final Context context, final Page page) {
            final DebugPane debugPane = context.getComponentFactory().createDebugPane();
            page.setDebug(debugPane);

            debugPane.addSection("Specification");
            final NakedObject object = context.getMappedObject(request.getObjectId());
            debugPane.appendln(Dump.specification(object));
        }

        public String name() {
            return "spec";
        }
    }

    private class DebugObject implements Action {
        public void execute(final Request request, final Context context, final Page page) {
            final DebugPane debugPane = context.getComponentFactory().createDebugPane();
            page.setDebug(debugPane);

            debugPane.addSection("Adapter");
            final NakedObject object = context.getMappedObject(request.getObjectId());
            debugPane.appendln(Dump.adapter(object));
            debugPane.addSection("Graph");
            debugPane.appendln(Dump.graph(object, NakedObjectsContext.getAuthenticationSession()));
        }

        public String name() {
            return "dump";
        }
    }
    
    private class DebugOn implements Action {
        private final WebController controller;

        public DebugOn(WebController controller) {
            this.controller = controller;
        }
        
        public void execute(Request request, Context context, Page page) {
          controller.setDebug(true);
        }

        public String name() {
            return "debugon";
        }
    }
    
    private class DebugOff implements Action {
        private final WebController controller;

        public DebugOff(WebController controller) {
            this.controller = controller;
        }
        
        public void execute(Request request, Context context, Page page) {
          controller.setDebug(false);
        }

        public String name() {
            return "debugoff";
        }
    }

    private final Map actions = new HashMap();
    private boolean isDebug;
    private final Logger LOG = Logger.getLogger(WebController.class);
    private final Logger ACCESS_LOG = Logger.getLogger("access_log");

    public boolean actionExists(final Request req) {
        return actions.containsKey(req.getRequestType());
    }

    protected void addAction(final Action action) {
        actions.put(action.name(), action);
    }

    private void addCrumbs(final Context context, final Page page) {
        final Crumb[] crumbs = context.getCrumbs();
        final String[] names = new String[crumbs.length];
        final boolean[] isLinked = context.isLinked();

        for (int i = 0; i < crumbs.length; i++) {
            names[i] = crumbs[i].title();
        }

        final ComponentFactory factory = context.getComponentFactory();
        final Component breadCrumbs = factory.createBreadCrumbs(names, isLinked);

        page.setCrumbs(breadCrumbs);
    }

    public void addDebug(final Page page, final Request req) {
        page.addDebug("<a href=\"debug.app\">Debug</a>");
        final String id = req.getObjectId();
        if (id != null) {
            page.addDebug("<a href=\"dump.app?id=" + id + "\">Object</a>");
            page.addDebug("<a href=\"spec.app?id=" + id + "\">Spec</a>");
        }
        page.addDebug("<a href=\"about.app\">About</a>");
        page.addDebug("<a href=\"debugoff.app\">Debug off</a>");        
    }

    public Page generatePage(final Context context, final Request request) {
        context.restoreAllObjectsToLoader();
        final Page page = context.getComponentFactory().createPage();
        pageHeader(context, page);
        final Block navigation = page.getNavigation();

        final Block optionBar = context.getComponentFactory().createBlock("options", null);
        optionBar.add(context.getComponentFactory().createHeading("Options"));

        Block block = context.getComponentFactory().createBlock("item", null);
        Component option = context.getComponentFactory().createLink("logout", "Log Out", "End the current session");
        block.add(option);
        optionBar.add(block);

        block = context.getComponentFactory().createBlock("item", null);
        option = context.getComponentFactory().createLink("about", "About", "Details about this application");
        block.add(option);
        optionBar.add(block);

        //boolean isExploring = SessionAccess.inExplorationMode();
        boolean isExploring = NakedObjectsContext.getDeploymentType().isExploring();
		if (isExploring) {
            block = context.getComponentFactory().createBlock("item", null);
            option = context.getComponentFactory().createLink("swapuser", "Swap User", "Swap the exploration user");
            block.add(option);
            optionBar.add(block);
        }

        navigation.add(optionBar);

        listServices(context, navigation);
        listHistory(context, navigation);
        Monitor.addEvent("Web", "Request " + request);
        runAction(context, request, page);
        addCrumbs(context, page);

        // The web viewer has no views of other objects, so changes can be ignored
        if (NakedObjectsContext.inSession() && 
            NakedObjectsContext.inTransaction()) {
        	NakedObjectsContext.getUpdateNotifier().clear();
        }
        // TODO deal with disposed objects

        return page;
    }

    public void init() {
        addAction(new About());
        addAction(new SwapUser());
        addAction(new SetUser());
        addAction(new DebugView());
        addAction(new DebugSpecification());
        addAction(new DebugObject());
        addAction(new Welcome());
        addAction(new ObjectView());
        addAction(new CollectionView());
        addAction(new FieldCollectionView());
        addAction(new InvokeMethod());
        addAction(new TaskStep());
        addAction(new EditObject());
        addAction(new Save());
        addAction(new ServiceView());
        addAction(new LogOut());
        addAction(new RemoveItemFromCollection());
        addAction(new AddItemToCollection());
        addAction(new ChangeContext());
        
        // TODO allow these to be exclude by configuration so they cannot be run in a real system
        addAction(new DebugOn(this));
        addAction(new DebugOff(this));

        Logger.getLogger(this.getClass()).info("init");
    }

    public boolean isDebug() {
        return isDebug;
    }

    private void listHistory(final Context context, final Block navigation) {
        context.listHistory(context, navigation);
    }

    private void listServices(final Context context, final Block navigationBar) {
        final Block taskBar = context.getComponentFactory().createBlock("services", null);
        taskBar.add(context.getComponentFactory().createHeading("Services"));
        AdapterManager adapterManager = NakedObjectsContext.getPersistenceSession().getAdapterManager();
        List<Object> services = getUserProfile().getPerspective().getServices();
        for (Object service : services) {
            NakedObject serviceAdapter = adapterManager.adapterFor(service); 
            if (serviceAdapter == null) {
            	LOG.warn("unable to find service Id: " + service + "; skipping");
            	continue;
            }
            if (isHidden(serviceAdapter)) {
                continue;
            }
            final String serviceMapId = context.mapObject(serviceAdapter);
            taskBar.add(createServiceComponent(context, serviceMapId, serviceAdapter));
        }
        navigationBar.add(taskBar);
    }

	private Component createServiceComponent(final Context context, final String serviceMapId, final NakedObject serviceNO) {
		String serviceName = serviceNO.titleString();
        String serviceIcon = serviceNO.getIconName();
        return context.getComponentFactory().createService(serviceMapId, serviceName, serviceIcon);
    }

    private boolean isHidden(final NakedObject serviceNO) {
        NakedObjectSpecification serviceNoSpec = serviceNO.getSpecification();
        boolean isHidden = serviceNoSpec.getFacet(HiddenFacet.class) != null;
        return isHidden;
    }

    private void pageHeader(final Context context, final Page page) {
        page.getPageHeader().add(context.getComponentFactory().createInlineBlock("none", "", null));
    }

    private void runAction(final Context context, final Request request, final Page page) {
        try {
            ACCESS_LOG.info("request " + request.toString());
            Request r = request;
            final DebugString debug = new DebugString();
            debug.startSection("Request");
            debug.appendln("http", request.toString());
            debug.endSection();
            do {
                final Action action = (Action) actions.get(r.getRequestType());
                try {
                    action.execute(r, context, page);
                } catch (final ObjectLookupException e) {
                    final String error = "The object/service you selected has timed out.  Please navigate to the object via the history bar.";
                    displayError(context, page, error);
                } catch (final TaskLookupException e) {
                    final String error = "The task you went back to has already been completed or cancelled.  Please start the task again.";
                    displayError(context, page, error);
                }
                r = r.getForward();
                if (r != null) {
                    LOG.debug("forward to " + r);
                }
            } while (r != null);
            if (LOG.isDebugEnabled()) {
                context.debug(debug);
                debug.appendln();
                if (NakedObjectsContext.inSession()) {
                    NakedObjectsContext.getSession(getExecutionContextId()).debugAll(debug);
                } else {
                    debug.appendln("No session");
                }
                LOG.debug(debug.toString());
            }
        } catch (final ActionException e) {
            page.setTitle("Error");
            page.getViewPane().setTitle("Error", "Action Exception");
            LOG.error("ActionException, executing action " + request.getRequestType(), e);
            page.getViewPane().add(context.getComponentFactory().createErrorMessage(e, isDebug));
        } catch (final NakedObjectApplicationException e) {
            page.setTitle("Error");
            page.getViewPane().setTitle("Error", "Application Exception");
            LOG.error("ApplicationException, executing action " + request.getRequestType(), e);
            page.getViewPane().add(context.getComponentFactory().createErrorMessage(e, isDebug));
        } catch (final NakedObjectException e) {
            page.setTitle("Error");
            page.getViewPane().setTitle("Error", "System Exception");
            LOG.error("NakedObjectRuntimeException, executing action " + request.getRequestType(), e);
            page.getViewPane().add(context.getComponentFactory().createErrorMessage(e, true));
        } catch (final RuntimeException e) {
            page.setTitle("Error");
            page.getViewPane().setTitle("Error", "System Exception");
            LOG.error("RuntimeException, executing action " + request.getRequestType(), e);
            page.getViewPane().add(context.getComponentFactory().createErrorMessage(e, true));
        }
    }

    private void displayError(final Context context, final Page page, final String errorMessage) {
        page.setTitle("Error");
        page.getViewPane().setTitle("Error", "");

        final Block block1 = context.getComponentFactory().createBlock("error", "");
        block1.add(context.getComponentFactory().createInlineBlock("", errorMessage, ""));
        page.getViewPane().add(block1);

        final Block block2 = context.getComponentFactory().createBlock("text", "");
        block2.add(context.getComponentFactory().createInlineBlock("", ERROR_REASON, ""));
        page.getViewPane().add(block2);
    }

    public void setDebug(final boolean on) {
        this.isDebug = on;
    }

    
    /////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    /////////////////////////////////////////////////////////
    
    private UserProfile getUserProfile() {
        return NakedObjectsContext.getUserProfile();
    }

    private String getExecutionContextId() {
        return NakedObjectsContext.getSessionId();
    }
}

// Copyright (c) Naked Objects Group Ltd.
