package org.nakedobjects.metamodel.commons.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceStreamSourceFileSystem extends ResourceStreamSourceAbstract {
	
	private String directory;

	public ResourceStreamSourceFileSystem(String directory) {
		this.directory = directory;
	}

	protected InputStream doReadResource(String resourcePath) throws FileNotFoundException {
		File file = new File(directory, resourcePath);
		return new FileInputStream(file);
	}

	@Override
	public OutputStream writeResource(String resourcePath) {
		File file = new File(directory, resourcePath);
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public String getName() {
		return "file system (directory '" + directory + "')";
	}

}
