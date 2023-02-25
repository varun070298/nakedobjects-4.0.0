package org.nakedobjects.runtime.system;

import java.util.List;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;

/**
 * Whether running on client or server side etc.
 */
public enum DeploymentType {

	EXPLORATION(DeploymentCategory.EXPLORING, ContextCategory.STATIC_RELAXED, SystemConstants.VIEWER_DEFAULT, Splash.SHOW),
	PROTOTYPE(DeploymentCategory.PROTOTYPING, ContextCategory.STATIC_RELAXED, SystemConstants.VIEWER_DEFAULT, Splash.SHOW),
	CLIENT(DeploymentCategory.PRODUCTION, ContextCategory.STATIC, SystemConstants.VIEWER_DEFAULT, Splash.SHOW),
	SERVER(DeploymentCategory.PRODUCTION, ContextCategory.THREADLOCAL, null, Splash.NO_SHOW),
	SERVER_EXPLORATION(DeploymentCategory.EXPLORING, ContextCategory.THREADLOCAL, null, Splash.NO_SHOW),
	SERVER_PROTOTYPE(DeploymentCategory.PROTOTYPING, ContextCategory.THREADLOCAL, null, Splash.NO_SHOW),
	SINGLE_USER(DeploymentCategory.PRODUCTION, ContextCategory.STATIC, SystemConstants.VIEWER_DEFAULT, Splash.NO_SHOW),
	UTILITY(DeploymentCategory.EXPLORING, ContextCategory.STATIC, null, Splash.NO_SHOW);

	private final DeploymentCategory deploymentCategory;
	private final ContextCategory contextCategory;
	private final String defaultViewer;
	private final Splash splash;

	private DeploymentType(final DeploymentCategory category, ContextCategory contextCategory, final String defaultViewer, Splash splash) {
		this.deploymentCategory = category;
		this.contextCategory = contextCategory;
		this.defaultViewer = defaultViewer;
		this.splash = splash;
	}

	public DebugInfo getDebug() {
	    return new DebugInfo() {

            public void debugData(DebugString debug) {
                debug.appendln("Category", deploymentCategory);
                debug.appendln("Context", contextCategory);
                debug.appendln("Default viewer", defaultViewer == null ? "none" : defaultViewer);
                debug.appendln("Show splash", splash);
                debug.appendln();
                debug.appendln("Name", friendlyName());
                debug.appendln("Can specify object store", canSpecifyObjectStore());
                debug.appendln("Can install fixtures", canInstallFixtures());
                debug.appendln("Should monitor", shouldMonitor());
            }

            public String debugTitle() {
                return "Deployment type";
            }
	    };
	}
	
	public void initContext(NakedObjectSessionFactory sessionFactory) {
		contextCategory.initContext(sessionFactory);
	}

	public boolean canSpecifyViewers(List<String> viewers) {
		return contextCategory.canSpecifyViewers(viewers);
	}

	public boolean canSpecifyConnectors(List<String> connectors) {
		return connectors.size() == 0 || this == CLIENT;
	}

	public boolean canSpecifyObjectStore() {
		return this != CLIENT;
	}

	public boolean canInstallFixtures() {
		return this != CLIENT;
	}

	public boolean shouldShowSplash() {
		return splash.isShow();
	}
	
	public boolean shouldMonitor() {
		return (this == SERVER) && isProduction();
	}

	public boolean isExploring() {
		return deploymentCategory == DeploymentCategory.EXPLORING;
	}

	public boolean isPrototyping() {
		return deploymentCategory == DeploymentCategory.PROTOTYPING;
	}

	public boolean isProduction() {
		return deploymentCategory == DeploymentCategory.PRODUCTION;
	}

	public void addDefaultViewer(List<String> requestedViewers) {
		if (requestedViewers.size() == 0 && defaultViewer != null) {
			requestedViewers.add(defaultViewer);
		}
	}
	
	/**
	 * similar to {@link #valueOf(String)}, but will convert any
	 * '-' to '_' first.
	 * 
	 * <p>
	 * For example, allows a {@link DeploymentType} to be specified as either <tt>server_exploration</tt>
	 * or as <tt>server-exploration</tt>.
	 */
	public static DeploymentType lookup(final String str) {
		String underscoredStr = str.replace('-', '_').toUpperCase();
		return DeploymentType.valueOf(underscoredStr);
	}
	
	public String friendlyName() {
		return name().toLowerCase().replace('_', '-');
	}

}