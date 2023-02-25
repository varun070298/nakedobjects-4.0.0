package org.nakedobjects.runtime.imageloader;

import org.nakedobjects.runtime.installers.InstallerAbstract;

public class TemplateImageLoaderNoopInstaller extends InstallerAbstract implements TemplateImageLoaderInstaller {

    public TemplateImageLoaderNoopInstaller() {
		super(TemplateImageLoaderInstaller.TYPE, "noop");
	}

    public TemplateImageLoader createLoader() {
    	return new TemplateImageLoaderNoop();
    }
    
}


// Copyright (c) Naked Objects Group Ltd.
