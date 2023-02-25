package org.nakedobjects.plugins.dnd.bootstrap;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceCreationException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.ConfigurationException;
import org.nakedobjects.plugins.dnd.HelpViewer;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.AwtToolkit;
import org.nakedobjects.plugins.dnd.viewer.LoginDialog;
import org.nakedobjects.plugins.dnd.viewer.ShutdownListener;
import org.nakedobjects.plugins.dnd.viewer.SkylarkViewFactory;
import org.nakedobjects.plugins.dnd.viewer.ViewerFrame;
import org.nakedobjects.plugins.dnd.viewer.XViewer;
import org.nakedobjects.plugins.dnd.viewer.basic.DragContentSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.EmptyField;
import org.nakedobjects.plugins.dnd.viewer.basic.InnerWorkspaceSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.PasswordFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.RootIconSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.RootWorkspaceSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.SubviewIconSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.WrappedTextFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.content.PerspectiveContent;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dnd.viewer.list.ExpandableListSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.GridSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.HistogramSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.InternalListSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.SimpleListSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.SummaryListWindowSpecification;
import org.nakedobjects.plugins.dnd.viewer.notifier.ViewUpdateNotifier;
import org.nakedobjects.plugins.dnd.viewer.table.WindowTableSpecification;
import org.nakedobjects.plugins.dnd.viewer.tree.ListWithDetailSpecification;
import org.nakedobjects.plugins.dnd.viewer.tree.TreeSpecification;
import org.nakedobjects.plugins.dnd.viewer.tree.TreeWithDetailSpecification;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.plugins.dnd.viewer.view.extendedform.TwoPartViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.field.CheckboxField;
import org.nakedobjects.plugins.dnd.viewer.view.field.ColorField;
import org.nakedobjects.plugins.dnd.viewer.view.field.ImageField;
import org.nakedobjects.plugins.dnd.viewer.view.field.TextFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.form.WindowFormSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.help.InternalHelpViewer;
import org.nakedobjects.plugins.dnd.viewer.view.message.DetailedMessageViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.message.MessageDialogSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.specification.ServiceIconSpecification;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.authentication.standard.fixture.AuthenticationRequestLogonFixture;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerAbstract;


public class DndViewer extends NakedObjectsViewerAbstract {
    
    private static final Logger LOG = Logger.getLogger(DndViewer.class);
    private static final String SPECIFICATION_BASE = Properties.PROPERTY_BASE + "specification.";
    private ViewUpdateNotifier updateNotifier;
    private ViewerFrame frame;
    private XViewer viewer;
    private ShutdownListener shutdownListener;
    private Bounds bounds;
    private HelpViewer helpViewer;
    private boolean acceptingLogIns = true;


    // ////////////////////////////////////
    // shutdown
    // ////////////////////////////////////

    @Override
    public void shutdown() {
        System.exit(0);
    }


    private Bounds calculateBounds(final Dimension screenSize) {
        int maxWidth = screenSize.width;
        final int maxHeight = screenSize.height;

        if ((screenSize.width / screenSize.height) >= 2) {
            final int f = screenSize.width / screenSize.height;
            maxWidth = screenSize.width / f;
        }

        final int width = maxWidth - 80;
        final int height = maxHeight - 80;
        final int x = 40;
        final int y = 40;

        Size defaultWindowSize = new Size(width, height);
        defaultWindowSize.limitWidth(800);
        defaultWindowSize.limitHeight(600);

        final Size size = Properties.getSize(Properties.PROPERTY_BASE + "initial.size", defaultWindowSize);
        final Location location = Properties.getLocation(Properties.PROPERTY_BASE + "initial.location", new Location(x, y));

        return new Bounds(location, size);
    }

    private ViewSpecification loadSpecification(final String name, final Class<?> cls) {
        final String factoryName = NakedObjectsContext.getConfiguration().getString(SPECIFICATION_BASE + name);
        ViewSpecification spec;
        if (factoryName != null) {
            spec = InstanceFactory.createInstance(factoryName, ViewSpecification.class);
        } else {
            spec = InstanceFactory.createInstance(cls.getName(), ViewSpecification.class);
        }
        return spec;
    }

    private synchronized void logOut() {
        LOG.info("user log out");
        saveDesktop();
        final AuthenticationSession session = NakedObjectsContext.getAuthenticationSession();
        getAuthenticationManager().closeSession(session);
        viewer.close();
        notify();
    }
    
    private void saveDesktop() {
    	if (!NakedObjectsContext.inSession()) {
    		// can't do anything
    		return;
    	} 
		// TODO get hold of desktop objects
		NakedObjectsContext.getUserProfileLoader().saveSession(new ArrayList<NakedObject>());
    }
    
    protected void quit() {
        LOG.info("user quit");
        saveDesktop();
        acceptingLogIns = false;
        shutdown();
    }

    @Override
    public synchronized void init() {
    	super.init();
    	
        new ImageFactory(NakedObjectsContext.getTemplateImageLoader());
        new AwtToolkit();
        setupViewFactory();

        setShutdownListener(new ShutdownListener() {
            public void logOut() {
                DndViewer.this.logOut();
            }

            public void quit() {
                DndViewer.this.quit();
            }
        });

        updateNotifier = new ViewUpdateNotifier();

        if (updateNotifier == null) {
            throw new NullPointerException("No update notifier set for " + this);
        }
        if (shutdownListener == null) {
            throw new NullPointerException("No shutdown listener set for " + this);
        }

        while (acceptingLogIns) {
            if (login()) {
                openViewer();
                try {
                    wait();
                } catch (final InterruptedException e) {}
            } else {
                quit();
            }
        }
    }

    // ////////////////////////////////////
    // login
    // ////////////////////////////////////

    
	// TODO: nasty
    private boolean loggedInUsingLogonFixture = false;
    
    /**
     * TODO: there is similar code in <tt>AuthenticationSessionLookupStrategyDefault</tt>;
     * should try to combine somehow... 
     */
    private boolean login() {
        AuthenticationRequest request = determineRequestIfPossible();

        // we may have enough to get a session
        AuthenticationSession session = getAuthenticationManager().authenticate(request);
        clearAuthenticationRequestViaArgs();
        
        // otherwise, keep prompting
        String message = null;
        while (session == null) {
            if (request == null) {
                request = promptForAuthenticationDetails(message);
            }
            if (request == null) {
                return false;
            }
            session = getAuthenticationManager().authenticate(request);
            // in case we need to try again
            request = null;
            message = "Invalid user/password; please try again";
        }
        
        NakedObjectsContext.openSession(session);
        return true;
    }


	private AuthenticationRequest determineRequestIfPossible() {
		AuthenticationRequest request;
		// command line args
        request = getAuthenticationRequestViaArgs();
        
        // exploration
        if (request == null && getDeploymentType().isExploring()) {
        	request = new AuthenticationRequestExploration(getLogonFixture());
        }
        
        // logon fixture provided
		if (request == null && getLogonFixture() != null && !loggedInUsingLogonFixture) {
			loggedInUsingLogonFixture = true;
			request = new AuthenticationRequestLogonFixture(getLogonFixture());
		}
		return request;
	}



	private AuthenticationRequest promptForAuthenticationDetails(String message) {
        final LoginDialog dialog = new LoginDialog(message);
        dialog.setVisible(true);
        dialog.toFront();
        if (dialog.login()) {
            dialog.setVisible(false);
            dialog.dispose();
            return new AuthenticationRequestPassword(dialog.getUser(), dialog.getPassword());
        } else {
            dialog.setVisible(false);
            dialog.dispose();
            return null;
        }
    }


    
    private void openViewer() {
        frame = new ViewerFrame();

        if (bounds == null) {
            bounds = calculateBounds(frame.getToolkit().getScreenSize());
        }

        frame.pack(); // forces insets to be calculated, hence need to then set bounds
        frame.setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());

        viewer = (XViewer) Toolkit.getViewer();
        viewer.setRenderingArea(frame);
        viewer.setUpdateNotifier(updateNotifier);
        viewer.setListener(shutdownListener);
        viewer.setExploration(isInExplorationMode());

        if (helpViewer == null) {
            helpViewer = new InternalHelpViewer(viewer);
        }
        viewer.setHelpViewer(helpViewer);

        frame.setViewer(viewer);

        final AuthenticationSession currentSession = NakedObjectsContext.getAuthenticationSession();
        if (currentSession == null) {
            throw new NullPointerException("No session for " + this);
        }

        final UserProfile userProfiler = NakedObjectsContext.getUserProfile();

        // TODO viewer should be shown during init() (so login can take place on main window, and can quit
        // before
        // logging in), and should be updated during start to show context.

        // TODO resolving should be done by the views?
        // resolveApplicationContextCollection(rootObject, "services");
        // resolveApplicationContextCollection(rootObject, "objects");
        final RootWorkspaceSpecification spec = new RootWorkspaceSpecification();
        final PerspectiveContent content = new PerspectiveContent(userProfiler.getPerspective());
        if (spec.canDisplay(content, new ViewRequirement(content, ViewRequirement.CLOSED))) {
            // View view = spec.createView(new RootObject(rootObject), null);
            final View view = spec.createView(content, null);
            viewer.setRootView(view);
        } else {
            throw new NakedObjectException();
        }

        viewer.init();

        final String name = userProfiler.getPerspective().getName();
        frame.setTitle(name);
        frame.init();

        viewer.initSize();
        viewer.scheduleRepaint();

        frame.setVisible(true);
        frame.toFront();
    }

    private boolean isInExplorationMode() {
        return getDeploymentType().isExploring();
    }

    public void setBounds(final Bounds bounds) {
        this.bounds = bounds;
    }

    public void setHelpViewer(final HelpViewer helpViewer) {
        this.helpViewer = helpViewer;
    }

    public void setShutdownListener(final ShutdownListener shutdownListener) {
        this.shutdownListener = shutdownListener;
    }

    private void setupViewFactory() throws ConfigurationException, InstanceCreationException {
        final SkylarkViewFactory viewFactory = (SkylarkViewFactory) Toolkit.getViewFactory();

        LOG.debug("setting up default views (provided by the framework)");

        /*
         * viewFactory.addValueFieldSpecification(loadSpecification("field.option",
         * OptionSelectionField.Specification.class));
         * viewFactory.addValueFieldSpecification(loadSpecification("field.percentage",
         * PercentageBarField.Specification.class));
         * viewFactory.addValueFieldSpecification(loadSpecification("field.timeperiod",
         * TimePeriodBarField.Specification.class));
         */
        viewFactory.addSpecification(loadSpecification("field.image", ImageField.Specification.class));
        viewFactory.addSpecification(loadSpecification("field.color", ColorField.Specification.class));
        viewFactory.addSpecification(loadSpecification("field.password", PasswordFieldSpecification.class));
        viewFactory.addSpecification(loadSpecification("field.wrappedtext", WrappedTextFieldSpecification.class));
        viewFactory.addSpecification(loadSpecification("field.checkbox", CheckboxField.Specification.class));
        viewFactory.addSpecification(loadSpecification("field.text", TextFieldSpecification.class));
        viewFactory.addSpecification(new RootWorkspaceSpecification());
        viewFactory.addSpecification(new InnerWorkspaceSpecification());

        if (NakedObjectsContext.getConfiguration().getBoolean(SPECIFICATION_BASE + "defaults", true)) {
            viewFactory.addSpecification(new InternalListSpecification());
            viewFactory.addSpecification(new SimpleListSpecification());
            viewFactory.addSpecification(new SummaryListWindowSpecification());
            viewFactory.addSpecification(new GridSpecification());
            // TBA viewFactory.addSpecification(new ListWithExpandableElementsSpecification());
            // TBA viewFactory.addSpecification(new CalendarSpecification());
            viewFactory.addSpecification(new ListWithDetailSpecification());
            viewFactory.addSpecification(new HistogramSpecification());
            
            viewFactory.addSpecification(new TreeWithDetailSpecification());
            viewFactory.addSpecification(new WindowFormSpecification());
            viewFactory.addSpecification(new WindowTableSpecification());
            // TBA viewFactory.addSpecification(new WindowExpandableFormSpecification());
            viewFactory.addSpecification(new TwoPartViewSpecification());
            // TBA viewFactory.addSpecification(new ExtendedFormSpecification());
            // TBA viewFactory.addSpecification(new FormWithDetailSpecification());
            
            viewFactory.addSpecification(new TreeSpecification());
            // TODO allow window form to be used for objects with limited number of collections 
            // viewFactory.addSpecification(new TreeWithDetailSpecification(0, 3));

            
            // viewFactory.addCompositeRootViewSpecification(new
            // BarchartSpecification());
            // viewFactory.addCompositeRootViewSpecification(new
            // GridSpecification());
        }

        viewFactory.addSpecification(new MessageDialogSpecification());
        viewFactory.addSpecification(new DetailedMessageViewSpecification());

        

        
        viewFactory.addEmptyFieldSpecification(loadSpecification("field.empty", EmptyField.Specification.class));

        viewFactory.addSpecification(loadSpecification("icon.object", RootIconSpecification.class));
        viewFactory.addSpecification(loadSpecification("icon.subview", SubviewIconSpecification.class));
        viewFactory.addSpecification(loadSpecification("icon.collection", ExpandableListSpecification.class));

        viewFactory.addSpecification(loadSpecification("icon.service", SubviewIconSpecification.class));
        viewFactory.addSpecification(loadSpecification("icon.service", ServiceIconSpecification.class));
        viewFactory.setDragContentSpecification(loadSpecification("drag-content", DragContentSpecification.class));

        final String viewParams = NakedObjectsContext.getConfiguration().getString(SPECIFICATION_BASE + "view");

        if (viewParams != null) {
            final StringTokenizer st = new StringTokenizer(viewParams, ",");

            while (st.hasMoreTokens()) {
                final String specName = st.nextToken().trim();

                if (specName != null && !specName.trim().equals("")) {
                    try {
                        ViewSpecification spec;
                        spec = (ViewSpecification) InstanceFactory.createInstance(specName);
                        LOG.info("adding view specification: " + spec);
                        viewFactory.addSpecification(spec);
                    } catch (final InstanceCreationException e) {
                        // LOG.error("failed to find view specification class " + specName);
                        throw e;
                    }
                }
            }
        }

    }

}
// Copyright (c) Naked Objects Group Ltd.
