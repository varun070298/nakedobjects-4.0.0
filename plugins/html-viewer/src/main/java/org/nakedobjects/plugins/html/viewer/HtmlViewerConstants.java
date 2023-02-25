package org.nakedobjects.plugins.html.viewer;

import org.nakedobjects.metamodel.config.ConfigurationConstants;

public final class HtmlViewerConstants {

	
    public static final String PROPERTY_BASE = 
    	ConfigurationConstants.ROOT + "viewer.html.";
    public static final String STYLE_SHEET = PROPERTY_BASE + "style-sheet";
    public static final String HEADER_FILE = PROPERTY_BASE + "header-file";
	/**
	 * Used if {@link #HEADER_FILE} is not specified or does not refer to a valid resource.
	 */
    public static final String HEADER = PROPERTY_BASE + "header";
    public static final String FOOTER_FILE = PROPERTY_BASE + "footer-file";
	/**
	 * Used if {@link #FOOTER_FILE} is not specified or does not refer to a valid resource.
	 */
    public static final String FOOTER = PROPERTY_BASE + "footer";

	public static final String VIEWER_HTML_RESOURCE_BASE_KEY = PROPERTY_BASE + "resourceBase";


	private HtmlViewerConstants() {}

}
