package org.nakedobjects.plugins.headless.junit.internal;

import org.junit.internal.runners.TestClass;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.runtime.authorization.standard.noop.NoopAuthorizationManagerInstaller;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.installers.NakedObjectsSystemUsingInstallers;
import org.nakedobjects.runtime.userprofile.inmemory.InMemoryUserProfileStoreInstaller;

public class NakedObjectsSystemUsingInstallersWithinJunit extends NakedObjectsSystemUsingInstallers {

    private final TestClass testClass;

    public NakedObjectsSystemUsingInstallersWithinJunit(
            final DeploymentType deploymentType, 
            final InstallerLookup installerLookup, 
            final TestClass testClass) {
        super(deploymentType, installerLookup);
        this.testClass = testClass;
        
        setAuthenticationInstaller(getInstallerLookup().injectDependenciesInto(new NoopAuthenticationManagerInstaller()));
        setAuthorizationInstaller(getInstallerLookup().injectDependenciesInto(new NoopAuthorizationManagerInstaller()));
        setPersistenceMechanismInstaller(getInstallerLookup().injectDependenciesInto(new InMemoryPersistenceMechanismInstallerWithinJunit()));
        setUserProfileStoreInstaller(getInstallerLookup().injectDependenciesInto(new InMemoryUserProfileStoreInstaller()));

        // fixture installer
        FixtureInstallerAnnotatedClass fixtureInstaller = new FixtureInstallerAnnotatedClass();
        try {
            fixtureInstaller.addFixturesAnnotatedOn(this.testClass.getJavaClass());
        } catch (InstantiationException e) {
            throw new NakedObjectException(e);
        } catch (IllegalAccessException e) {
            throw new NakedObjectException(e);
        }
        setFixtureInstaller(fixtureInstaller);

        // services installer
        ServicesInstallerAnnotatedClass servicesInstaller = new ServicesInstallerAnnotatedClass();
        try {
            servicesInstaller.addServicesAnnotatedOn(this.testClass.getJavaClass());
        } catch (InstantiationException e) {
            throw new NakedObjectException(e);
        } catch (IllegalAccessException e) {
            throw new NakedObjectException(e);
        }
		setServicesInstaller(servicesInstaller);
    }

    public TestClass getTestClass() {
		return testClass;
	}
    

}


// Copyright (c) Naked Objects Group Ltd.
