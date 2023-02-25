package org.nakedobjects.runtime.imageloader.awt;

import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;



public class TemplateImageLoaderAwtInstaller extends InstallerAbstract implements TemplateImageLoaderInstaller {

    public TemplateImageLoaderAwtInstaller() {
		super(TemplateImageLoaderInstaller.TYPE, "awt");
	}

    public TemplateImageLoader createLoader() {
    	return new TemplateImageLoaderAwt(getConfiguration());
    }
    
}

// Copyright (c) Naked Objects Group Ltd.
