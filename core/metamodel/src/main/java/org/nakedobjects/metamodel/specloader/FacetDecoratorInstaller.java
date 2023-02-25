package org.nakedobjects.metamodel.specloader;

import java.util.List;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;



public interface FacetDecoratorInstaller extends Installer {

	static String TYPE = "facet-decorator";

    List<FacetDecorator> createDecorators();
}

// Copyright (c) Naked Objects Group Ltd.
