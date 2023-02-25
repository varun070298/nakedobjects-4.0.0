package org.nakedobjects.runtime.i18n.resourcebundle;

import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public class ResourceBasedI18nDecoratorInstaller extends InstallerAbstract implements FacetDecoratorInstaller {

    public ResourceBasedI18nDecoratorInstaller() {
		super(FacetDecoratorInstaller.TYPE, "resource-i18n");
	}
    
	public List<FacetDecorator> createDecorators() {
        final ResourceBasedI18nManager manager = new ResourceBasedI18nManager(getConfiguration());
        return Arrays.<FacetDecorator>asList(new I18nFacetDecorator(manager));
    }
}
