package org.nakedobjects.plugins.html.viewer;

import org.nakedobjects.metamodel.commons.lang.MapUtils;
import org.nakedobjects.plugins.html.servlet.ControllerServlet;
import org.nakedobjects.plugins.html.servlet.HtmlServletConstants;
import org.nakedobjects.plugins.html.servlet.LogonServlet;
import org.nakedobjects.runtime.NakedObjects;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstallerAbstract;
import org.nakedobjects.runtime.web.EmbeddedWebViewer;
import org.nakedobjects.runtime.web.WebAppSpecification;
import org.nakedobjects.webapp.NakedObjectsSessionFilter;
import org.nakedobjects.webapp.StaticContentFilter;
import org.nakedobjects.webapp.servlets.ResourceServlet;


/**
 * Convenience implementation of a {@link NakedObjectsViewer} providing the
 * ability to run a Jetty web server configured for the HTML viewer from the
 * {@link NakedObjects command line}.
 * 
 * <p>
 * To run, use the <tt>--viewer html</tt> flag.
 * 
 * <p>
 * In a production deployment the configuration represented by the
 * {@link WebAppSpecification} would be specified in the <tt>web.xml<tt> file.
 */
public class HtmlViewerInstaller extends NakedObjectsViewerInstallerAbstract {
	
	private static final String LOGON_PAGE = HtmlServletConstants.LOGON_APP_PAGE;
	private static final String LOGON_PAGE_MAPPED = "/"+LOGON_PAGE;
	
	private static final String[] STATIC_CONTENT = new String[]{"*.gif", "*.png", "*.jpg", "*.css"};
	private static final String DYNAMIC_CONTENT = "*.app";


	public HtmlViewerInstaller() {
		super("html");
	}
	
	
	@Override
    public NakedObjectsViewer doCreateViewer() {
        return new EmbeddedWebViewer() {
            public WebAppSpecification getWebAppSpecification() {

                WebAppSpecification webAppSpec = new WebAppSpecification();
                
                webAppSpec.addFilterSpecification(
                		NakedObjectsSessionFilter.class, 
                		MapUtils.asMap(NakedObjectsSessionFilter.LOGON_PAGE_KEY, LOGON_PAGE_MAPPED), 
                		DYNAMIC_CONTENT);
                webAppSpec.addServletSpecification(LogonServlet.class, LOGON_PAGE_MAPPED);
                webAppSpec.addServletSpecification(ControllerServlet.class, DYNAMIC_CONTENT);
                
                webAppSpec.addFilterSpecification(StaticContentFilter.class, STATIC_CONTENT);
                webAppSpec.addServletSpecification(ResourceServlet.class, STATIC_CONTENT );
                

                final String resourceBaseDir = getConfiguration().getString(HtmlViewerConstants.VIEWER_HTML_RESOURCE_BASE_KEY);
                if (resourceBaseDir != null) {
                    webAppSpec.addResourcePath(resourceBaseDir);
                }
                webAppSpec.addResourcePath("./src/main/resources");
                webAppSpec.addResourcePath("./src/main/webapp");
                webAppSpec.addResourcePath("./web");
                webAppSpec.addResourcePath(".");
                webAppSpec.addWelcomeFile(LOGON_PAGE);
                
				webAppSpec.setLogHint("open a web browser and browse to logon.app to connect");

                return webAppSpec;
            }
        };        
    }

}

// Copyright (c) Naked Objects Group Ltd.

