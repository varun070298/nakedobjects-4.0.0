package org.nakedobjects.runtime.help.file;

import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.runtime.help.StandardHelpFacetDecorator;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public class FileBasedHelpDecoratorInstaller extends InstallerAbstract implements FacetDecoratorInstaller {

    public FileBasedHelpDecoratorInstaller() {
		super(FacetDecoratorInstaller.TYPE, "help-file");
	}

	public List<FacetDecorator> createDecorators() {
        final FileBasedHelpManager manager = new FileBasedHelpManager(getConfiguration());
        return Arrays.<FacetDecorator>asList(new StandardHelpFacetDecorator(manager));
    }

}
