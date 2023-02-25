package org.nakedobjects.runtime.imageloader;

import org.nakedobjects.metamodel.commons.component.Installer;


public interface TemplateImageLoaderInstaller extends Installer {

	static String TYPE = "template-image-loader";
	
    TemplateImageLoader createLoader();
}

// Copyright (c) Naked Objects Group Ltd.
