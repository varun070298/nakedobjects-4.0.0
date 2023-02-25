package org.nakedobjects.runtime.persistence.adapterfactory;

import org.nakedobjects.metamodel.commons.component.Installer;


public interface AdapterFactoryInstaller extends Installer {

	static String TYPE = "adapter-factory";

    AdapterFactory createAdapterFactory();
}

// Copyright (c) Naked Objects Group Ltd.
