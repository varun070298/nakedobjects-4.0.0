package org.nakedobjects.runtime.transaction.facetdecorator.standard;

import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.runtime.transaction.facetdecorator.TransactionFacetDecoratorInstallerAbstract;


public class TransactionFacetDecoratorInstaller extends TransactionFacetDecoratorInstallerAbstract {

    public TransactionFacetDecoratorInstaller() {
        super("transaction");
    }

    public List<FacetDecorator> createDecorators() {
        return Arrays.<FacetDecorator>asList(
        		new StandardTransactionFacetDecorator(getConfiguration()));
    }

}
