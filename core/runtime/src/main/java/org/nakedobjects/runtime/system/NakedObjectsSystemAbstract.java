package org.nakedobjects.runtime.system;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.commons.component.NoopUtils;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationSession;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.imageloader.awt.TemplateImageLoaderAwt;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.system.internal.InitialisationSession;
import org.nakedobjects.runtime.system.internal.NakedObjectsLocaleInitializer;
import org.nakedobjects.runtime.system.internal.NakedObjectsTimeZoneInitializer;
import org.nakedobjects.runtime.system.internal.SplashWindow;
import org.nakedobjects.runtime.userprofile.UserProfileStore;


/**
 * 
 */
public abstract class NakedObjectsSystemAbstract implements NakedObjectsSystem {

    public static final Logger LOG = Logger.getLogger(NakedObjectsSystemAbstract.class);

    private static final int SPLASH_DELAY_DEFAULT = 6;

    private final NakedObjectsLocaleInitializer localeInitializer;
    private final NakedObjectsTimeZoneInitializer timeZoneInitializer;
    private final DeploymentType deploymentType;

    private SplashWindow splashWindow;

    private FixturesInstaller fixtureInstaller;
    
    private boolean initialized = false;

    private NakedObjectSessionFactory sessionFactory;

    private LogonFixture logonFixture;

    // ///////////////////////////////////////////
    // Constructors
    // ///////////////////////////////////////////

    public NakedObjectsSystemAbstract(final DeploymentType deploymentType) {
        this(deploymentType, new NakedObjectsLocaleInitializer(), new NakedObjectsTimeZoneInitializer());
    }

    public NakedObjectsSystemAbstract(
            final DeploymentType deploymentType,
            final NakedObjectsLocaleInitializer localeInitializer,
            final NakedObjectsTimeZoneInitializer timeZoneInitializer) {
        this.deploymentType = deploymentType;
        this.localeInitializer = localeInitializer;
        this.timeZoneInitializer = timeZoneInitializer;
    }

    // ///////////////////////////////////////////
    // DeploymentType
    // ///////////////////////////////////////////

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    // ///////////////////////////////////////////
    // init, shutdown
    // ///////////////////////////////////////////

    public void init() {

        if (initialized) {
            throw new IllegalStateException("Already initialized");
        } else {
            initialized = true;
        }

        LOG.info("initialising naked objects system");
        LOG.info("working directory: " + new File(".").getAbsolutePath());
        LOG.info("resource stream source: " + getConfiguration().getResourceStreamSource());

        localeInitializer.initLocale(getConfiguration());
        timeZoneInitializer.initTimeZone(getConfiguration());

        int splashDelay = SPLASH_DELAY_DEFAULT;
        try {
            TemplateImageLoader splashLoader = obtainTemplateImageLoader();
            splashLoader.init();
            showSplash(splashLoader);

            // REVIEW is this the best way to make the configuration available?
            NakedObjectsContext.setConfiguration(getConfiguration());
            
            sessionFactory = doCreateSessionFactory(deploymentType);
            
            NakedObjectsContext.setConfiguration(sessionFactory.getConfiguration());

            initContext(sessionFactory);
            sessionFactory.init();

            installFixturesIfRequired();
            
        } catch (NakedObjectSystemException ex) {
            LOG.error("failed to initialise", ex);
            splashDelay = 0;
            throw new RuntimeException(ex);
        } finally {
            removeSplash(splashDelay);
        }
    }

	private void installFixturesIfRequired() throws NakedObjectSystemException {
		// some deployment types (eg CLIENT) do not support installing fixtures
		// instead, any fixtures should be installed when server boots up.
		if (!deploymentType.canInstallFixtures()) {
			return;
		}
		
		fixtureInstaller = obtainFixturesInstaller();
		if (fixtureInstaller == null || NoopUtils.isNoop(fixtureInstaller)) {
			return;
		}
		
		NakedObjectsContext.openSession(new InitialisationSession());
		fixtureInstaller.installFixtures();
		try {
			
				
				// only allow logon fixtures if not in production mode.
				if (!deploymentType.isProduction()) {
					logonFixture = fixtureInstaller.getLogonFixture();
				}
		} finally {
			NakedObjectsContext.closeSession();
		}
	}

    private void initContext(NakedObjectSessionFactory sessionFactory) {
    	getDeploymentType().initContext(sessionFactory);
    }

    public void shutdown() {
        LOG.info("shutting down system");
        NakedObjectsContext.closeAllSessions();
    }

    // ///////////////////////////////////////////
    // Hook:
    // ///////////////////////////////////////////

    /**
     * Hook method; the returned implementation is expected to use the same general approach as the subclass
     * itself.
     * 
     * <p>
     * So, for example, <tt>NakedObjectsSystemUsingInstallers</tt> uses the {@link InstallerLookup} mechanism
     * to find its components. The corresponding <tt>ExecutionContextFactoryUsingInstallers</tt> object
     * returned by this method should use {@link InstallerLookup} likewise.
     */
    protected abstract NakedObjectSessionFactory doCreateSessionFactory(final DeploymentType deploymentType)
            throws NakedObjectSystemException;

    // ///////////////////////////////////////////
    // Configuration
    // ///////////////////////////////////////////

    /**
     * Populated after {@link #init()}.
     */
    public NakedObjectSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // ///////////////////////////////////////////
    // Configuration
    // ///////////////////////////////////////////

    public abstract NakedObjectConfiguration getConfiguration();

    // ///////////////////////////////////////////
    // TemplateImageLoader
    // ///////////////////////////////////////////

    /**
     * Just returns a {@link TemplateImageLoaderAwt}; subclasses may override if
     * required. 
     */
    protected TemplateImageLoader obtainTemplateImageLoader() {
        return new TemplateImageLoaderAwt(getConfiguration());
    }

    // ///////////////////////////////////////////
    // Reflector
    // ///////////////////////////////////////////

    protected abstract NakedObjectReflector obtainReflector(DeploymentType deploymentType) throws NakedObjectSystemException;

    // ///////////////////////////////////////////
    // PersistenceSessionFactory
    // ///////////////////////////////////////////

    protected abstract PersistenceSessionFactory obtainPersistenceSessionFactory(DeploymentType deploymentType)
            throws NakedObjectSystemException;

    // ///////////////////////////////////////////
    // Fixtures
    // ///////////////////////////////////////////

    /**
     * This is the only {@link Installer} that is used by any (all) subclass implementations, because it
     * effectively <i>is</i> the component we need (as opposed to a builder/factory of the component we need).
     * 
     * <p>
     * The fact that the component <i>is</i> an installer (and therefore can be {@link InstallerLookup} looked
     * up} is at this level really just an incidental implementation detail useful for the subclass that uses
     * {@link InstallerLookup} to create the other components.
     */
    protected abstract FixturesInstaller obtainFixturesInstaller() throws NakedObjectSystemException;

    // ///////////////////////////////////////////
    // Authentication Manager
    // ///////////////////////////////////////////

    protected abstract AuthenticationManager obtainAuthenticationManager(DeploymentType deploymentType)
            throws NakedObjectSystemException;

    
    // ///////////////////////////////////////////
    // UserProfileLoader
    // ///////////////////////////////////////////

    protected abstract UserProfileStore obtainUserProfileStore();

    // ///////////////////////////////////////////
    // Services
    // ///////////////////////////////////////////

    protected abstract List<Object> obtainServices();


    // ///////////////////////////////////////////
    // Fixtures Installer
    // ///////////////////////////////////////////

    public FixturesInstaller getFixturesInstaller() {
        return fixtureInstaller;
    }

    
    /**
     * The {@link LogonFixture}, if any, obtained by running fixtures.
     * 
     * <p>
     * Intended to be used when for {@link DeploymentType#EXPLORATION exploration} (instead
     * of an {@link ExplorationSession}) or {@link DeploymentType#PROTOTYPE prototype} deployments
     * (saves logging in).  Should be <i>ignored</i> in other {@link DeploymentType}s.
     */
    public LogonFixture getLogonFixture() {
		return logonFixture;
	}

    

    // ///////////////////////////////////////////
    // Splash
    // ///////////////////////////////////////////

    private void showSplash(TemplateImageLoader imageLoader) {
    	
        boolean vetoSplashFromConfig = getConfiguration().getBoolean(SystemConstants.NOSPLASH_KEY, SystemConstants.NOSPLASH_DEFAULT);
		if (!vetoSplashFromConfig && getDeploymentType().shouldShowSplash()) {
            splashWindow = new SplashWindow(imageLoader);
        }
    }

    private void removeSplash(final int delay) {
        if (splashWindow != null) {
            if (delay == 0) {
                splashWindow.removeImmediately();
            } else {
                splashWindow.toFront();
                splashWindow.removeAfterDelay(delay);
            }
        }
    }

    // ///////////////////////////////////////////
    // debugging
    // ///////////////////////////////////////////

    private void debug(final DebugString debug, final Object object) {
        if (object instanceof DebugInfo) {
            final DebugInfo d = (DebugInfo) object;
            debug.appendTitle(d.debugTitle());
            d.debugData(debug);
        } else {
            debug.appendln(object.toString());
            debug.appendln("... no further debug information");
        }
    }

    public DebugInfo debugSection(String selectionName) {
        //DebugInfo deb;
        if (selectionName.equals("Configuration")) {
             return getConfiguration();
        } /*else if (selectionName.equals("Overview")) {
            debugOverview(debug);
        } else  if (selectionName.equals("Authenticator")) {
            deb = NakedObjectsContext.getAuthenticationManager();
        } else if (selectionName.equals("Reflector")) {
            deb = NakedObjectsContext.getSpecificationLoader();
        } else if (selectionName.equals("Contexts")) {
            deb = debugListContexts(debug);
        } else {
            deb = debugDisplayContext(selectionName, debug);
        }*/
        return null;
    }
    
    private void debugDisplayContext(final String selector, final DebugString debug) {
        final NakedObjectSession d = NakedObjectsContext.getSession(selector);
        if (d != null) {
            d.debugAll(debug);
        } else {
            debug.appendln("No context: " + selector);
        }
    }

    private void debugListContexts(final DebugString debug) {
        final String[] contextIds = NakedObjectsContext.getInstance().allSessionIds();
        for (int i = 0; i < contextIds.length; i++) {
            debug.appendln(contextIds[i]);
            debug.appendln("-----");
            final NakedObjectSession d = NakedObjectsContext.getSession(contextIds[i]);
            d.debug(debug);
            debug.appendln();
        }
    }

    public String[] debugSectionNames() {
        final String[] general = new String[] { "Overview", "Authenticator", "Configuration", "Reflector", "Requests",
                "Contexts" };
        final String[] contextIds = NakedObjectsContext.getInstance().allSessionIds();
        final String[] combined = new String[general.length + contextIds.length];
        System.arraycopy(general, 0, combined, 0, general.length);
        System.arraycopy(contextIds, 0, combined, general.length, contextIds.length);
        return combined;
    }

    private void debugOverview(final DebugString debug) {
        try {
            debug.appendln(AboutNakedObjects.getFrameworkName());
            debug.appendln(AboutNakedObjects.getFrameworkVersion());
            if (AboutNakedObjects.getApplicationName() != null) {
                debug.appendln("application: " + AboutNakedObjects.getApplicationName());
            }
            if (AboutNakedObjects.getApplicationVersion() != null) {
                debug.appendln("version" + AboutNakedObjects.getApplicationVersion());
            }

            final String user = System.getProperty("user.name");
            final String system = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") "
                    + System.getProperty("os.version");
            final String java = System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version");
            debug.appendln("user: " + user);
            debug.appendln("os: " + system);
            debug.appendln("java: " + java);
            debug.appendln("working directory: " + new File(".").getAbsolutePath());

            debug.appendTitle("System Installer");
            debug.appendln("Fixture Installer", fixtureInstaller == null ? "none" : fixtureInstaller.getClass().getName());

            debug.appendTitle("System Components");
            debug.appendln("Authentication manager", NakedObjectsContext.getAuthenticationManager().getClass().getName());
            debug.appendln("Configuration", getConfiguration().getClass().getName());

            final DebugInfo[] inf = NakedObjectsContext.debugSystem();
            for (int i = 0; i < inf.length; i++) {
                if (inf[i] != null) {
                    inf[i].debugData(debug);
                }
            }
        } catch (RuntimeException e) {
            debug.appendException(e);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
