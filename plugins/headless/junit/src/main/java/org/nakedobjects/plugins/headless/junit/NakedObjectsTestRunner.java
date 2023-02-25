package org.nakedobjects.plugins.headless.junit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.MethodRoadie;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.plugins.headless.junit.internal.NakedObjectsSystemUsingInstallersWithinJunit;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderDefault;
import org.nakedobjects.metamodel.config.ConfigurationBuilderFileSystem;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.standard.SimpleSession;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationSession;
import org.nakedobjects.runtime.authentication.standard.fixture.AuthenticationRequestLogonFixture;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.installers.InstallerLookupDefault;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.Splash;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


/**
 * Copied from JMock, and with the same support.
 * 
 */
public class NakedObjectsTestRunner extends JUnit4ClassRunner {

    private final Field mockeryField;


    /**
     * Only used during object construction.
     */
    public NakedObjectsTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);

        // JMock initialization, adapted to allow for no mockery field.
        mockeryField = findFieldAndMakeAccessible(testClass, Mockery.class);
    }

    @Override
    protected void invokeTestMethod(final Method method, final RunNotifier notifier) {

    	final Description description = methodDescription(method);
    	
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilderDefault();
        configurationBuilder.add(SystemConstants.NOSPLASH_KEY, ""+true); // switch off splash
        
        InstallerLookupDefault installerLookup = new InstallerLookupDefault(getClass());
        configurationBuilder.injectInto(installerLookup);
		installerLookup.init();

		NakedObjectsSystemUsingInstallersWithinJunit system = null;        
		AuthenticationSession session = null;
        try {
            // init the system; cf similar code in NakedObjects and NakedObjectsServletContextInitializer
            final DeploymentType deploymentType = DeploymentType.PROTOTYPE;

            // TODO: replace with regular NakedObjectsSystem and remove this subclass.
            system = new NakedObjectsSystemUsingInstallersWithinJunit(
                    deploymentType, installerLookup, getTestClass());

            system.init();
            
			// specific to this bootstrap mechanism
            AuthenticationRequest request;
			final LogonFixture logonFixture = system.getFixturesInstaller().getLogonFixture();
			if (logonFixture != null) {
				request = new AuthenticationRequestLogonFixture(logonFixture);
			} else {
				request = new AuthenticationRequestExploration(logonFixture);
			}
			session = NakedObjectsContext.getAuthenticationManager().authenticate(request);
			
			NakedObjectsContext.openSession(session);
			getTransactionManager().startTransaction();
			
			Object test = createTest();
			getServicesInjector().injectDependencies(test);
			
		    final TestMethod testMethod = wrapMethod(method);
		    new MethodRoadie(test, testMethod, notifier, description).run();
		    
		    getTransactionManager().endTransaction();
            
        } catch (final InvocationTargetException e) {
            notifier.testAborted(description, e.getCause());
            getTransactionManager().abortTransaction();
            return;
        } catch (final Exception e) {
            notifier.testAborted(description, e);
            return;
        } finally {
            if (system != null) {
                if (session != null) {
                    NakedObjectsContext.closeSession();
                }
                system.shutdown();
            }
        }
    }


    /**
     * Taken from JMock's runner.
     */
    @Override
    protected TestMethod wrapMethod(final Method method) {
        return new TestMethod(method, getTestClass()) {
            @Override
            public void invoke(final Object testFixture) throws IllegalAccessException, InvocationTargetException {

                super.invoke(testFixture);

                if (mockeryField != null) {
                    mockeryOf(testFixture).assertIsSatisfied();
                }
            }
        };
    }

    /**
     * JMock code.
     * 
     * @param test
     * @return
     */
    protected Mockery mockeryOf(final Object test) {
        if (mockeryField == null) {
            return null;
        }
        try {
            final Mockery mockery = (Mockery) mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException(String.format("Mockery named '%s' is null", mockeryField.getName()));
            }
            return mockery;
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(String.format("cannot get value of field %s", mockeryField.getName()), e);
        }
    }

    /**
     * Adapted from JMock code.
     */
    static Field findFieldAndMakeAccessible(final Class<?> testClass, final Class<?> clazz) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (final Field field : c.getDeclaredFields()) {
                if (clazz.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }
    

    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
    
    private static ServicesInjector getServicesInjector() {
        return getPersistenceSession().getServicesInjector();
    }

    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }


}
