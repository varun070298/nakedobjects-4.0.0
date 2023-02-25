package org.nakedobjects.runtime.installers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.metamodel.commons.about.ComponentDetails;
import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;

/**
 * Details name and version of installer.
 */
public class InstallerVersion implements ComponentDetails {

    private final Installer installer;

    public InstallerVersion(Installer installer) {
        this.installer = installer;}

    public String getName() {
        return installer.getName();
    }
    
    public String getModule() {
       return  "org.nakedobjects.plugins:dndviewer";
    }
    
    public String getVersion() {
        return findVersion(getModule());
    }
    
    public boolean isInstalled() {
        return false;
    }
    
    private String findVersion(final String moduleId) {
        try {
            String module = moduleId.replace(":", "/");
            InputStream resourceAsStream = AboutNakedObjects.class.getClassLoader().getResourceAsStream("META-INF/maven/" + module + "/pom.properties");
            if (resourceAsStream == null) {
                return "no version";
            }
            Properties p = new Properties();
            p.load(resourceAsStream);
            String version = p.getProperty("version");
            return version;
        } catch (IOException e) {
           throw new NakedObjectException(e);
        }
    }

}


// Copyright (c) Naked Objects Group Ltd.
