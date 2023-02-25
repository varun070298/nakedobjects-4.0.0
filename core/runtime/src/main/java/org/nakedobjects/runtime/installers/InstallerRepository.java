package org.nakedobjects.runtime.installers;

import org.nakedobjects.metamodel.commons.component.Installer;

public interface InstallerRepository {

    public Installer[] getInstallers(final Class<?> cls);

}


// Copyright (c) Naked Objects Group Ltd.
