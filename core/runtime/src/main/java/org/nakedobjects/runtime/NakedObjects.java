package org.nakedobjects.runtime;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.nakedobjects.metamodel.commons.threads.ThreadRunner;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.options.standard.OptionHandlerDeploymentType;
import org.nakedobjects.runtime.options.standard.OptionHandlerDeploymentTypeNakedObjects;
import org.nakedobjects.runtime.options.standard.OptionHandlerPassword;
import org.nakedobjects.runtime.options.standard.OptionHandlerUser;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.system.NakedObjectsSystemBootstrapper;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebServer;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;
import org.nakedobjects.runtime.web.WebAppSpecification;


public class NakedObjects extends NakedObjectsAbstract {
    private static final String DEFAULT_EMBEDDED_WEBSERVER = SystemConstants.WEBSERVER_DEFAULT;

    private OptionHandlerUser flagHandlerUser;
    private OptionHandlerPassword flagHandlerPassword;
    private OptionHandlerDeploymentType flagHandlerDeploymentType;

    public static void main(final String[] args) {
        new NakedObjects().run(args);
    }

    protected void addOptionHandlers(InstallerLookup installerLookup) {
        super.addOptionHandlers(installerLookup);

        addOptionHandler(flagHandlerDeploymentType = createOptionHandlerDeploymentType());

        addOptionHandler(flagHandlerUser = new OptionHandlerUser());
        addOptionHandler(flagHandlerPassword = new OptionHandlerPassword());

    }

    protected OptionHandlerDeploymentType createOptionHandlerDeploymentType() {
        return new OptionHandlerDeploymentTypeNakedObjects();
    }

    protected DeploymentType deploymentType() {
        return flagHandlerDeploymentType.getDeploymentType();
    }

    protected boolean validateUserAndPasswordCombo() {
        String user = flagHandlerUser.getUserName();
        String password = flagHandlerPassword.getPassword();
        return password == null && user == null || password != null && user != null;
    }

    /**
     * Overridable.
     */
    protected void bootstrapNakedObjects(
            InstallerLookup installerLookup,
            DeploymentType deploymentType,
            List<String> viewerNames) {
        List<NakedObjectsViewer> viewers = lookupViewers(installerLookup, viewerNames, deploymentType);
        bootstrapSystem(installerLookup, deploymentType);
        bootstrapViewers(installerLookup, viewers);
    }

    private List<NakedObjectsViewer> lookupViewers(
            InstallerLookup installerLookup,
            List<String> viewerNames,
            DeploymentType deploymentType) {
        List<String> viewersToStart = new ArrayList<String>(viewerNames);
        deploymentType.addDefaultViewer(viewersToStart);

        List<NakedObjectsViewer> viewers = new ArrayList<NakedObjectsViewer>();
        for (String requestedViewer : viewersToStart) {
            final NakedObjectsViewerInstaller viewerInstaller = installerLookup.viewerInstaller(requestedViewer);
            final NakedObjectsViewer viewer = viewerInstaller.createViewer();
            viewers.add(viewer);
        }
        return viewers;
    }

    /**
     * Bootstrap the {@link NakedObjectsSystem}, injecting into all {@link NakedObjectsViewer viewer}s.
     */
    private void bootstrapSystem(InstallerLookup installerLookup, DeploymentType deploymentType) {
        NakedObjectsSystemBootstrapper bootstrapper = new NakedObjectsSystemBootstrapper(installerLookup);
        bootstrapper.bootSystem(deploymentType);
    }


    private void bootstrapViewers(InstallerLookup installerLookup, List<NakedObjectsViewer> viewers) {
        // split viewers into web viewers and non-web viewers
        List<NakedObjectsViewer> webViewers = findWebViewers(viewers);
        List<NakedObjectsViewer> nonWebViewers = findNonWebViewers(viewers, webViewers);

        startNonWebViewers(nonWebViewers);
        startWebViewers(installerLookup, webViewers);
    }

    private List<NakedObjectsViewer> findWebViewers(List<NakedObjectsViewer> viewers) {
        List<NakedObjectsViewer> webViewers = new ArrayList<NakedObjectsViewer>(viewers);
        CollectionUtils.filter(webViewers, new Predicate() {
            public boolean evaluate(Object object) {
                NakedObjectsViewer viewer = (NakedObjectsViewer) object;
                return viewer.getWebAppSpecification() != null;
            }
        });
        return webViewers;
    }

    private List<NakedObjectsViewer> findNonWebViewers(List<NakedObjectsViewer> viewers, List<NakedObjectsViewer> webViewers) {
        List<NakedObjectsViewer> nonWebViewers = new ArrayList<NakedObjectsViewer>(viewers);
        nonWebViewers.removeAll(webViewers);
        return nonWebViewers;
    }

    /**
     * Starts each (non web) {@link NakedObjectsViewer viewer} in its own thread.
     */
    private void startNonWebViewers(List<NakedObjectsViewer> viewers) {
        for (final NakedObjectsViewer viewer : viewers) {
            Runnable target = new Runnable() {
                public void run() {
                    viewer.init();
                }
            };
            new ThreadRunner().startThread(target, "Viewer");
        }
    }

    /**
     * Starts all the web {@link NakedObjectsViewer viewer}s in an instance of an {@link EmbeddedWebServer}.
     */
    private void startWebViewers(final InstallerLookup installerLookup, final List<NakedObjectsViewer> webViewers) {
        if (webViewers.size() == 0) {
            return;
        }

        // TODO: we could potentially offer pluggability here
        EmbeddedWebServerInstaller webServerInstaller = installerLookup.embeddedWebServerInstaller(DEFAULT_EMBEDDED_WEBSERVER);
        EmbeddedWebServer embeddedWebServer = webServerInstaller.createEmbeddedWebServer();
        for (final NakedObjectsViewer viewer : webViewers) {
            WebAppSpecification webContainerRequirements = viewer.getWebAppSpecification();
            embeddedWebServer.addWebAppSpecification(webContainerRequirements);
        }
        embeddedWebServer.init();
    }

}
// Copyright (c) Naked Objects Group Ltd.
