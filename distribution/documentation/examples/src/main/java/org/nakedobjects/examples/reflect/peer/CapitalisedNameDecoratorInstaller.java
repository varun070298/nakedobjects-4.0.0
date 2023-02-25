package org.nakedobjects.examples.reflect.peer;

import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public class CapitalisedNameDecoratorInstaller extends InstallerAbstract implements FacetDecoratorInstaller {

    public CapitalisedNameDecoratorInstaller() {
		super(FacetDecoratorInstaller.TYPE, "capital-name");
	}

	public List<FacetDecorator> createDecorators() {
        return Arrays.<FacetDecorator>asList(new CapitalisedNameFacetDecorator());
    }
    
}
