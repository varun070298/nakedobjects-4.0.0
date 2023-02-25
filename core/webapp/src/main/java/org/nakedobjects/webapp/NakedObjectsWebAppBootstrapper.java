package org.nakedobjects.webapp;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceContextLoaderClassPath;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAbstract;
import org.nakedobjects.metamodel.config.ConfigurationPrimer;
import org.nakedobjects.metamodel.config.NotFoundPolicy;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.logging.NakedObjectsLoggingConfigurer;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.system.NakedObjectsSystemBootstrapper;
import org.nakedobjects.runtime.system.SystemConstants;


/**
 * Initialize the {@link NakedObjectsSystem} when the web application starts, and destroys it when it ends.
 * 
 * <p>
 * Implementation note: we use a number of helper builders to keep this class as small and focused as
 * possible. The builders are available for reuse by other bootstrappers.
 */
public class NakedObjectsWebAppBootstrapper implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(NakedObjectsWebAppBootstrapper.class);

    
    /**
     * Convenience for servlets that need to obtain the {@link NakedObjectsSystem}.
     */
    public static NakedObjectsSystem getSystemBoundTo(ServletContext servletContext) {
    	Object system = servletContext.getAttribute(WebAppConstants.NAKED_OBJECTS_SYSTEM_KEY);
		return (NakedObjectsSystem) system;
    }
    
    
    ///////////////////////////////////////////////////////
    // Initialization
    ///////////////////////////////////////////////////////
    
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        String webInfDir = servletContext.getRealPath("/WEB-INF");
        NakedObjectsLoggingConfigurer.configureLogging(webInfDir, new String[0]);

        // will load either from WEB-INF or from the classpath.
		final ConfigurationBuilder configurationBuilder = 
			new ConfigurationBuilderAbstract(
					new ResourceStreamSourceServletContext(servletContext),
					new ResourceStreamSourceContextLoaderClassPath());

        primeConfigurationBuilder(configurationBuilder, servletContext);
		final DeploymentType deploymentType = determineDeploymentType(configurationBuilder, servletContext);

		addConfigurationResourcesForWebApps(configurationBuilder);
		addConfigurationResourcesForDeploymentType(configurationBuilder, deploymentType);
        
		InstallerLookup installerLookup = 
			NakedObjectsSystemBootstrapper.createAndInitializeInstallerLookup(configurationBuilder, getClass());
		NakedObjectsSystem system = bootstrapNakedObjects(installerLookup, deploymentType);
        
        servletContext.setAttribute(WebAppConstants.NAKED_OBJECTS_SYSTEM_KEY, system);
        
        LOG.info("server started");
    }

    @SuppressWarnings("unchecked")
	private void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder, ServletContext servletContext) {
    	List<ConfigurationPrimer> configurationPrimers = 
    		(List<ConfigurationPrimer>) servletContext.getAttribute("nakedobjects.configurationPrimers");
    	if (configurationPrimers == null) {
    		return;
    	}
    	for (ConfigurationPrimer configurationPrimer : configurationPrimers) {
			configurationPrimer.primeConfigurationBuilder(configurationBuilder);
		}
	}


	/**
	 * Checks {@link ConfigurationBuilder configuration} for {@value SystemConstants#DEPLOYMENT_TYPE_KEY},
	 * (that is, from the command line), but otherwise searches in the {@link ServletContext}, first
	 * for {@value WebAppConstants#DEPLOYMENT_TYPE_KEY} and also {@value SystemConstants#DEPLOYMENT_TYPE_KEY}.
	 * 
	 * <p>
	 * If no setting is found, defaults to {@value WebAppConstants#DEPLOYMENT_TYPE_DEFAULT}.
     */
    private DeploymentType determineDeploymentType(ConfigurationBuilder configurationBuilder, final ServletContext servletContext) {
    	String deploymentTypeStr = configurationBuilder.getConfiguration().getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    	if (deploymentTypeStr == null) {
    		deploymentTypeStr = servletContext.getInitParameter(WebAppConstants.DEPLOYMENT_TYPE_KEY);
    	}
    	if (deploymentTypeStr == null) {
        	deploymentTypeStr = servletContext.getInitParameter(SystemConstants.DEPLOYMENT_TYPE_KEY);
    	}
    	if (deploymentTypeStr == null) {
    		deploymentTypeStr = WebAppConstants.DEPLOYMENT_TYPE_DEFAULT;
    	}
    	return DeploymentType.lookup(deploymentTypeStr);
    }
    
	private void addConfigurationResourcesForDeploymentType(
			final ConfigurationBuilder configurationLoader,
			final DeploymentType deploymentType) {
		String type = deploymentType.name().toLowerCase();
    	configurationLoader.addConfigurationResource(type + ".properties", NotFoundPolicy.CONTINUE);
	}

	private void addConfigurationResourcesForWebApps(
			final ConfigurationBuilder configurationLoader) {
		for (String config : (new String[] { "web.properties", "war.properties" })) {
    		if (config != null) {
    			configurationLoader.addConfigurationResource(config, NotFoundPolicy.CONTINUE);
    		}
    	}
	}
    
	private NakedObjectsSystem bootstrapNakedObjects(
			final InstallerLookup installerLookup, final DeploymentType deploymentType) {
        NakedObjectsSystemBootstrapper bootstrapper = 
        	new NakedObjectsSystemBootstrapper(installerLookup);
		return bootstrapper.bootSystem(deploymentType);
	}

	
    ///////////////////////////////////////////////////////
    // Destroy
    ///////////////////////////////////////////////////////

    public void contextDestroyed(final ServletContextEvent ev) {
        LOG.info("server shutting down");
        ServletContext servletContext = ev.getServletContext();
        
        try {
        	final NakedObjectsSystem system = (NakedObjectsSystem) servletContext.getAttribute(WebAppConstants.NAKED_OBJECTS_SYSTEM_KEY);
        	if (system != null) {
        		LOG.info("calling system shutdown");
        		system.shutdown();
        	}
        } finally {
        	servletContext.removeAttribute(WebAppConstants.NAKED_OBJECTS_SYSTEM_KEY);
        	LOG.info("server shut down");
        }
    }
    

}

// Copyright (c) Naked Objects Group Ltd.
