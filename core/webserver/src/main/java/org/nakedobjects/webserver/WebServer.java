package org.nakedobjects.webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ArrayUtils;
import org.nakedobjects.metamodel.commons.lang.CastUtils;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationPrimer;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.NakedObjectsAbstract;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.options.standard.OptionHandlerDeploymentType;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.webserver.internal.FlagHandlerAddress;
import org.nakedobjects.webserver.internal.FlagHandlerDeploymentTypeWebServer;
import org.nakedobjects.webserver.internal.FlagHandlerPort;
import org.nakedobjects.webserver.internal.FlagHandlerResourceBase;

import static org.nakedobjects.webserver.WebServerConstants.EMBEDDED_WEB_SERVER_PORT_DEFAULT;
import static org.nakedobjects.webserver.WebServerConstants.EMBEDDED_WEB_SERVER_PORT_KEY;
import static org.nakedobjects.webserver.WebServerConstants.EMBEDDED_WEB_SERVER_RESOURCE_BASE_DEFAULT;
import static org.nakedobjects.webserver.WebServerConstants.EMBEDDED_WEB_SERVER_RESOURCE_BASE_KEY;


public class WebServer extends NakedObjectsAbstract {

    public static void main(String[] args) {
        new WebServer().run(ArrayUtils.append(args, "--nosplash"));
    }

    @Override
    protected void addOptionHandlers(InstallerLookup installerLookup) {
        super.addOptionHandlers(installerLookup);
        addOptionHandler(new FlagHandlerPort());
        addOptionHandler(new FlagHandlerAddress());
        addOptionHandler(new FlagHandlerResourceBase());
        addOptionHandler(new FlagHandlerDeploymentTypeWebServer());
    }

    @Override
    protected OptionHandlerDeploymentType createOptionHandlerDeploymentType() {
        return new FlagHandlerDeploymentTypeWebServer(false);
    }
    
    protected boolean validateUserAndPasswordCombo() {
        return true;
    }
        
    protected DeploymentType deploymentType() {
        return DeploymentType.SERVER;
    }

    /**
     * ignores the arguments and just bootstraps JettyViewer, come what may.
     */
    @Override
    protected void bootstrapNakedObjects(
            InstallerLookup installerLookup,
            DeploymentType deploymentType,
            List<String> viewerNames) {

        ConfigurationBuilder configurationBuilder = installerLookup.getConfigurationBuilder();

        // we don't bootstrap the system here; instead we expect it to be bootstrapped
        // from the ServletContextInitializer in the web.xml
        NakedObjectConfiguration configuration = configurationBuilder.getConfiguration();
        int port = configuration.getInteger(EMBEDDED_WEB_SERVER_PORT_KEY, EMBEDDED_WEB_SERVER_PORT_DEFAULT);
        String webappContextPath = configuration.getString(EMBEDDED_WEB_SERVER_RESOURCE_BASE_KEY,
                EMBEDDED_WEB_SERVER_RESOURCE_BASE_DEFAULT);

        Server server = new Server(port);
        WebAppContext context = new WebAppContext("src/main/webapp", webappContextPath);

        copyConfigurationPrimersIntoServletContext(context);

        server.setHandler(context);
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            throw new NakedObjectException("Unable to start Jetty server", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void copyConfigurationPrimersIntoServletContext(WebAppContext context) {
        List<ConfigurationPrimer> configurationPrimers = (List<ConfigurationPrimer>) (List<?>) getFlagHandlers();
        context.setAttribute("nakedobjects.configurationPrimers", configurationPrimers);
    }

    @SuppressWarnings("unused")
    private void copyDeploymentTypeIntoInitParams(WebAppContext context) {
        Map<String, String> initParams = CastUtils.cast(context.getInitParams());
        initParams = new HashMap<String, String>(initParams);
        context.setInitParams(initParams);
    }

}
