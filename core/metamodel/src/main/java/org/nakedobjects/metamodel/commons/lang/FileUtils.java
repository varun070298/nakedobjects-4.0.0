package org.nakedobjects.metamodel.commons.lang;

import java.io.File;


public final class FileUtils {

    private FileUtils() {}

	public static boolean fileExists(final String fileName) {
	    return new File(fileName).exists();
	}


}

// Copyright (c) Naked Objects Group Ltd.
