package org.nakedobjects.metamodel.commons.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ResourceStreamSourceComposite extends ResourceStreamSourceAbstract {

	private static Logger LOG = Logger.getLogger(ResourceStreamSourceComposite.class);

	private List<ResourceStreamSource> resourceStreamSources = new ArrayList<ResourceStreamSource>();
	
	public ResourceStreamSourceComposite(ResourceStreamSource... resourceStreamSources) {
		for(ResourceStreamSource rss: resourceStreamSources) {
			addResourceStreamSource(rss);
		}
	}
	
	
	public void addResourceStreamSource(ResourceStreamSource rss) {
		this.resourceStreamSources.add(rss);
	}
	
	protected InputStream doReadResource(String resourcePath) {
		for(ResourceStreamSource rss: resourceStreamSources) {
			InputStream resourceStream = rss.readResource(resourcePath);
			if (resourceStream != null) {
				return resourceStream;
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("could not load resource path '" + resourcePath + "' from " + getName() );
		}
		return null;
	}

	@Override
	public OutputStream writeResource(String resourcePath) {
		for(ResourceStreamSource rss: resourceStreamSources) {
			OutputStream os = rss.writeResource(resourcePath);
			if (os != null) {
				return os;
			}
		}
		return null;
	}
	
	public String getName() {
		return "[" + resourceStreamNames() + "]";
	}

	private String resourceStreamNames() {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (ResourceStreamSource rss : resourceStreamSources) {
			if (first) {
				first = false;
			} else {
				buf.append(", ");
			}
			buf.append(rss.getName());
		}
		return buf.toString();
	}



}
