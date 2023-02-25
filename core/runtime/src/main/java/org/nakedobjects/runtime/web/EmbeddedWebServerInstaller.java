package org.nakedobjects.runtime.web;

import org.nakedobjects.metamodel.commons.component.Installer;

public interface EmbeddedWebServerInstaller extends Installer {

	static String TYPE = "embedded-web-server";

    EmbeddedWebServer createEmbeddedWebServer();
}


// Copyright (c) Naked Objects Group Ltd.
