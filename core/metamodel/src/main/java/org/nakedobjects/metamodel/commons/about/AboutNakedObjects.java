package org.nakedobjects.metamodel.commons.about;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class AboutNakedObjects {
    private static String applicationCopyrightNotice;
    private static String applicationName;
    private static String applicationVersion;
    private static String frameworkName;
    private static String frameworkVersion;
    private static String logo;
    private static String frameworkCopyright;
    private static String frameworkCompileDate;
    private static List<ComponentDetails> componentDetails;

    static {
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle("nof-version");
            logo = bundle.getString("framework.logo");
            frameworkVersion = bundle.getString("framework.version");
            frameworkName = bundle.getString("framework.name");
            frameworkCopyright = bundle.getString("framework.copyright");
            frameworkCompileDate = bundle.getString("framework.compile.date");
        } catch (final MissingResourceException ex) {
            logo = "splash-logo";
            frameworkVersion = "${project.version}-r${buildNumber}";
            frameworkCopyright = "Copyright (c) 2002~2009 Naked Objects Group";
            frameworkName = "${project.parent.name}";
        }

        // NOT in use yet: frameworkVersion = findVersion();
    }

    public static String findVersion() {
        try {
            String moduleId = "org.nakedobjects.plugins:dndviewer";
            String module = moduleId.replace(":", "/");
            InputStream resourceAsStream = AboutNakedObjects.class.getClassLoader().getResourceAsStream(
                    "META-INF/maven/" + module + "/pom.properties");
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

    public static String getApplicationCopyrightNotice() {
        return applicationCopyrightNotice;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static String getApplicationVersion() {
        return applicationVersion;
    }

    public static String getFrameworkCopyrightNotice() {
        return select(frameworkCopyright, "Copyright Naked Objects Group");
    }

    public static String getFrameworkCompileDate() {
        return frameworkCompileDate;
    }

    public static String getFrameworkName() {
        return select(frameworkName, "Naked Objects Framework");
    }

    public static String getImageName() {
        return select(logo, "splash-logo");
    }

    public static String getFrameworkVersion() {
        String version = "Version " + select(frameworkVersion, "unreleased");
        /* NOT in use yet:
        for (ComponentDetails details : componentDetails) {
            version += "\n" + details.getName() + " " + details.getModule() + " " + details.getVersion();
        }
        */
        return version;
    }

    public static void main(final String[] args) {
        System.out.println(getFrameworkName() + ", " + getFrameworkVersion());
        System.out.println(getFrameworkCopyrightNotice());
    }

    private static String select(final String value, final String defaultValue) {
        return value == null || value.startsWith("${") ? defaultValue : value;
    }

    public static void setApplicationCopyrightNotice(final String applicationCopyrightNotice) {
        AboutNakedObjects.applicationCopyrightNotice = applicationCopyrightNotice;
    }

    public static void setApplicationName(final String applicationName) {
        AboutNakedObjects.applicationName = applicationName;
    }

    public static void setApplicationVersion(final String applicationVersion) {
        AboutNakedObjects.applicationVersion = applicationVersion;
    }

    public static void setComponentDetails(List<ComponentDetails> componentDetails) {
        AboutNakedObjects.componentDetails = componentDetails;
    }

}
// Copyright (c) Naked Objects Group Ltd.
