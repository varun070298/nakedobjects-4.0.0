package org.nakedobjects.webapp.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.lang.IoUtils;
import org.nakedobjects.metamodel.commons.lang.Resources;
import org.nakedobjects.metamodel.commons.lang.StringUtils;


/**
 * 
 * @author Dan Haywood
 *
 */
public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Logger LOG = Logger.getLogger(ResourceServlet.class);

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        processRequest(request, response);
    }

    private void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final String servletPath = StringUtils.stripLeadingSlash(request.getServletPath());
        LOG.info("request: " + servletPath);
        
        // try to load from classpath
        InputStream is = Resources.getResourceAsStream(servletPath);
        if (is != null) {
            IoUtils.copy(is, response.getOutputStream());
            is.close();
            return;
        }
        
        // otherwise, try to load from filesystem
        is = getRealPath(request);
        if (is != null) {
            IoUtils.copy(is, response.getOutputStream());
            is.close();
            return;
        }
        
    }

	private FileInputStream getRealPath(final HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath(request.getServletPath());
        try {
			return new FileInputStream(realPath);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
