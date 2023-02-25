package org.nakedobjects.runtime.transaction.facetdecorator;

import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public abstract class TransactionFacetDecoratorInstallerAbstract extends InstallerAbstract implements FacetDecoratorInstaller {
    
    public TransactionFacetDecoratorInstallerAbstract(final String name) {
    	super(FacetDecoratorInstaller.TYPE, name);
    }

}
