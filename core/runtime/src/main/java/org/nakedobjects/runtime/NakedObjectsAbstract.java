package org.nakedobjects.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderDefault;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.ConfigurationPrimer;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.installers.InstallerLookupDefault;
import org.nakedobjects.runtime.logging.NakedObjectsLoggingConfigurer;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandler;
import org.nakedobjects.runtime.options.standard.OptionHandlerAdditionalProperty;
import org.nakedobjects.runtime.options.standard.OptionHandlerConfiguration;
import org.nakedobjects.runtime.options.standard.OptionHandlerConnector;
import org.nakedobjects.runtime.options.standard.OptionHandlerDebug;
import org.nakedobjects.runtime.options.standard.OptionHandlerDeploymentType;
import org.nakedobjects.runtime.options.standard.OptionHandlerDiagnostics;
import org.nakedobjects.runtime.options.standard.OptionHandlerFixture;
import org.nakedobjects.runtime.options.standard.OptionHandlerHelp;
import org.nakedobjects.runtime.options.standard.OptionHandlerNoSplash;
import org.nakedobjects.runtime.options.standard.OptionHandlerPersistor;
import org.nakedobjects.runtime.options.standard.OptionHandlerQuiet;
import org.nakedobjects.runtime.options.standard.OptionHandlerReflector;
import org.nakedobjects.runtime.options.standard.OptionHandlerUserProfileStore;
import org.nakedobjects.runtime.options.standard.OptionHandlerVerbose;
import org.nakedobjects.runtime.options.standard.OptionHandlerVersion;
import org.nakedobjects.runtime.options.standard.OptionHandlerViewer;
import org.nakedobjects.runtime.system.DeploymentType;


public abstract class NakedObjectsAbstract {
    private List<OptionHandler> optionHandlers = new ArrayList<OptionHandler>();
    private OptionHandlerConnector flagHandlerClientConnection;
    private OptionHandlerPersistor flagHandlerPersistor;
    private OptionHandlerViewer flagHandlerViewer;

    protected final void run(final String[] args) {
        setupLoggingImmediately(args);
        InstallerLookup installerLookup = new InstallerLookupDefault(NakedObjectsAbstract.class);

        addOptionHandlers(installerLookup);

        // add options
        final Options options = new Options();
        for (OptionHandler optionHandler : optionHandlers) {
            optionHandler.addOption(options);
        }

        // parse & handle flags
        BootPrinter printer = new BootPrinter(getClass());
        final CommandLineParser parser = new BasicParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            for (OptionHandler optionHandler : optionHandlers) {
                if (!optionHandler.handle(commandLine, printer, options)) {
                    return;
                }
            }
        } catch (final ParseException e) {
            printer.printErrorMessage(e.getMessage());
            printer.printHelp(options);
            return;
        }

        if (!validateUserAndPasswordCombo()) {
            printer.printErrorAndHelp(options, "A user name must be specified with a password");
            return;
        }

        // adhoc validation
        DeploymentType deploymentType = deploymentType();

        List<String> viewerNames = flagHandlerViewer.getViewerNames();
        String objectPersistorName = flagHandlerPersistor.getPersistorName();
        List<String> connectorNames = flagHandlerClientConnection.getConnectorNames();

        if (!validateDeploymentTypeAndConnectors(options, printer, deploymentType, connectorNames)) {
            return;
        }
        if (!validateDeploymentTypeAndViewers(options, printer, deploymentType, viewerNames)) {
            return;
        }
        if (!validateDeploymentTypeAndPersistor(options, printer, deploymentType, objectPersistorName)) {
            return;
        }

        // prime configuration
        ConfigurationBuilder builder = createConfigurationBuilder();
        for (ConfigurationPrimer optionHandler : optionHandlers) {
            optionHandler.primeConfigurationBuilder(builder);
        }

        // wire up and initialize installer lookup
        builder.injectInto(installerLookup);
        installerLookup.init();

        bootstrapNakedObjects(installerLookup, deploymentType, viewerNames);
    }

    private void setupLoggingImmediately(final String[] args) {
        String configDirectory = ConfigurationConstants.DEFAULT_CONFIG_DIRECTORY;
        NakedObjectsLoggingConfigurer.configureLogging(configDirectory, args);
    }

    protected abstract DeploymentType deploymentType();

    protected abstract boolean validateUserAndPasswordCombo();

    public List<OptionHandler> getFlagHandlers() {
        return Collections.unmodifiableList(optionHandlers);
    }

    protected void addOptionHandlers(InstallerLookup installerLookup) {

        addOptionHandler(new OptionHandlerConfiguration());

        addOptionHandler(flagHandlerClientConnection = new OptionHandlerConnector(installerLookup));
        addOptionHandler(flagHandlerPersistor = new OptionHandlerPersistor(installerLookup));
        addOptionHandler(new OptionHandlerReflector(installerLookup));
        addOptionHandler(flagHandlerViewer = new OptionHandlerViewer(installerLookup));

        addOptionHandler(new OptionHandlerUserProfileStore(installerLookup));

        addOptionHandler(new OptionHandlerFixture());
        addOptionHandler(new OptionHandlerNoSplash());
        addOptionHandler(new OptionHandlerAdditionalProperty());

        addOptionHandler(new OptionHandlerDebug());
        addOptionHandler(new OptionHandlerDiagnostics());
        addOptionHandler(new OptionHandlerQuiet());
        addOptionHandler(new OptionHandlerVerbose());

        addOptionHandler(new OptionHandlerHelp());
        addOptionHandler(new OptionHandlerVersion());

    }

    protected abstract OptionHandlerDeploymentType createOptionHandlerDeploymentType();

    protected boolean addOptionHandler(OptionHandler optionHandler) {
        return optionHandlers.add(optionHandler);
    }

    private boolean validateDeploymentTypeAndPersistor(
            final Options options,
            final BootPrinter printer,
            final DeploymentType deploymentType,
            final String objectPersistor) {
        if (deploymentType.canSpecifyObjectStore() || StringUtils.isEmpty(objectPersistor)) {
            return true;
        }
        printer.printErrorAndHelp(options, "Error: cannot specify an object store (persistor) for deployment type %s\n",
                deploymentType.name().toLowerCase());
        return false;
    }

    private boolean validateDeploymentTypeAndViewers(
            final Options options,
            final BootPrinter printer,
            final DeploymentType deploymentType,
            List<String> viewers) {
        if (deploymentType.canSpecifyViewers(viewers)) {
            return true;
        }
        printer.printErrorAndHelp(options, "Error: cannot specify %s viewer%s for deployment type %s\n",
                (viewers.size() > 1 ? "more than one" : "any"), (viewers.size() > 1 ? "" : "s"), deploymentType.name()
                        .toLowerCase());
        return false;
    }

    private boolean validateDeploymentTypeAndConnectors(
            final Options options,
            final BootPrinter printer,
            final DeploymentType deploymentType,
            final List<String> connectors) {
        if (deploymentType.canSpecifyConnectors(connectors)) {
            return true;
        }
        printer.printErrorAndHelp(options, "Error: cannot specify %s connector%s for deployment type %s\n",
                (connectors.size() > 1 ? "more than one" : "any"), (connectors.size() > 1 ? "" : "s"), deploymentType.name()
                        .toLowerCase());
        return false;
    }

    protected abstract void bootstrapNakedObjects(
            InstallerLookup installerLookup,
            DeploymentType deploymentType,
            List<String> viewerNames);


    /**
     * Hook method, possibly overridable.
     * 
     * <p>
     * The default implementation returns a {@link ConfigurationBuilderDefault}, which looks to the
     * <tt>config/</tt> directory, the <tt>src/main/webapp/WEB-INF</tt> directory, and then finally to the
     * classpath. However, this could be a security concern in a production environment; a user could edit the
     * <tt>nakedobjects.properties</tt> config files to disable security, for example.
     * 
     * <p>
     * This hook method therefore allows this {@link NakedObjectsAbstract} class to be subclassed and setup to
     * use a different {@link ConfigurationBuilder}. For example, a security-conscious subclass could return a
     * {@link ConfigurationBuilder} that only reads from the classpath. This would allow the application to be
     * deployed as a single sealed JAR that could not be tampered with.
     */
    protected ConfigurationBuilderDefault createConfigurationBuilder() {
        return new ConfigurationBuilderDefault();
    }

    // ///////////////////////////////////////////////////////////////////////////////////////

}
// Copyright (c) Naked Objects Group Ltd.
