package org.nakedobjects.runtime.context;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.testsystem.TestProxyPersistenceSession;
import org.nakedobjects.runtime.testsystem.TestProxyReflector;
import org.nakedobjects.runtime.testsystem.TestProxySession;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public class NakedObjectsContextTest {

    private Mockery mockery = new JUnit4Mockery();
    
    private NakedObjectConfiguration configuration;
    private PersistenceSession persistenceSession;
    private NakedObjectReflector reflector;
    private TestProxySession session;

    protected TemplateImageLoader mockTemplateImageLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    private UserProfileLoader mockUserProfileLoader;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;

	private List<Object> servicesList;


    @Before
    public void setUp() throws Exception {
        NakedObjectsContext.testReset();

        servicesList = Collections.emptyList();

        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        
        configuration = new PropertiesConfiguration();
        reflector = new TestProxyReflector();
        persistenceSession = new TestProxyPersistenceSession(mockPersistenceSessionFactory);

        mockery.checking(new Expectations() {{
            one(mockPersistenceSessionFactory).createPersistenceSession();
            will(returnValue(persistenceSession));
        
            ignoring(mockPersistenceSessionFactory);
            
            one(mockUserProfileLoader).getProfile(with(any(AuthenticationSession.class)));
            will(returnValue(new UserProfile()));
            
            ignoring(mockUserProfileLoader);
            
            ignoring(mockAuthenticationManager);

            ignoring(mockAuthorizationManager);

            ignoring(mockTemplateImageLoader);
        }});

    	reflector.setRuntimeContext(new RuntimeContextFromSession());

    	NakedObjectsContext.setConfiguration(configuration);
    	
        NakedObjectSessionFactory sessionFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    configuration, 
                    mockTemplateImageLoader, 
                    reflector, 
                    mockAuthenticationManager, 
                    mockAuthorizationManager, 
                    mockUserProfileLoader, 
                    mockPersistenceSessionFactory, servicesList);
        NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
        sessionFactory.init();
        
        session = new TestProxySession();
        NakedObjectsContext.openSession(session);
    }

    @Test
    public void testConfiguration() {
        Assert.assertEquals(configuration, NakedObjectsContext.getConfiguration());
    }

    @Test
    public void testObjectPersistor() {
        Assert.assertEquals(persistenceSession, NakedObjectsContext.getPersistenceSession());
    }

    @Test
    public void testSpecificationLoader() {
        Assert.assertEquals(reflector, NakedObjectsContext.getSpecificationLoader());
    }

    @Test
    public void testSession() {
        Assert.assertEquals(session, NakedObjectsContext.getAuthenticationSession());
    }
}
// Copyright (c) Naked Objects Group Ltd.
