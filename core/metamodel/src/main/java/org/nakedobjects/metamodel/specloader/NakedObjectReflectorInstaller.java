package org.nakedobjects.metamodel.specloader;

import org.nakedobjects.metamodel.commons.component.Installer;


/**
 * Installs a {@link NakedObjectReflector}during system start up.
 */
public interface NakedObjectReflectorInstaller extends Installer {

	static String TYPE = "reflector";

    NakedObjectReflector createReflector();
    
    void addFacetDecoratorInstaller(final FacetDecoratorInstaller decoratorInstaller);


}
// Copyright (c) Naked Objects Group Ltd.
